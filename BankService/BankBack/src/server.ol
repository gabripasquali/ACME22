include "console.iol"
include "interface.iol"
include "string_utils.iol"
include "database.iol"

inputPort Bank {
    Location: "socket://localhost:8000"
    Protocol: soap {
        .wsdl = "BankService.wsdl";
        .wsdl.port = "Bank"
    }
    Interfaces: interface
}

cset{
	sid: 
		loginResponse.sid
		payRequest.sid
		logoutRequest.sid
		verifyTokenRequest.sid
		refoundRequest.sid
		confirmRequest.sid
}

execution{ concurrent }

init{
    println@Console("Bank server is running...")();
	keepRunning = false
	//setup BankDB
	with(connectionInfo){
		.username = "bank";
		.password = "bank";
		.host = "bank-db";
		.database = "postgres";
		.driver = "postgresql"
	};
	connect@Database(connectionInfo)();
	
	scope (createTable){
		install(SQLException => println@Console("tables already exists")());
		update@Database(
		  "CREATE TABLE IF NOT EXISTS Users("+
				"id         SERIAL     PRIMARY KEY, " +
				"username   TEXT        NOT NULL        UNIQUE, " +
				"password   TEXT        NOT NULL, " +
				"balance    REAL        NOT NULL );"
		  )(result1)
		update@Database(
		  "CREATE TABLE IF NOT EXISTS Transactions(" +
				"token      TEXT        NOT NULL        UNIQUE, " +
				"amount     REAL        NOT NULL , " +
				"from_user  INTEGER     NOT NULL , " +
				"to_user  	INTEGER     NOT NULL , " +
				"status     INTEGER     NOT NULL , " +
				"order_id   INTEGER     NOT NULL , " +
    			"PRIMARY KEY (to_user, order_id) , " +
				"CHECK (status >= 0 AND status <= 4), " +
				"FOREIGN KEY (from_user) REFERENCES Users(id), " +
				"FOREIGN KEY (to_user) REFERENCES Users(id) );"
		)(result2)
	}
	scope (update){
		install(SQLException => println@Console("error during insert")());
		update@Database(
			"INSERT INTO Users (username, password, balance) VALUES (:username1, :password1, :balance1), (:username2, :password2, :balance2)" {
				.username1 = "demo",
				.password1 = "demo",
				.balance1 = 25.80,
				.username2 = "acme",
				.password2 = "acme",
				.balance2 = 0.0
			}
		)(ret)
	}
	query@Database("SELECT * FROM Users")(queryResponse);
	println@Console("queryResponse: " + queryResponse)()
	println@Console("user:" + queryResponse.row[0].username)()
	
}


main{
    
	login(loginRequest)(loginResponse){
		username = loginRequest.username;
		password = loginRequest.password;
		flag = false
				
		println@Console("login request with usn "+username+" and psw "+ password)();
		query@Database("SELECT * FROM users WHERE username='"+username+"' and password='"+password+"'")(queryResponse);
		if (queryResponse.row[0].username == null){
			println@Console("User not exist")()
			loginResponse.success = false
		}else{
			loginResponse.sid = csets.sid = new;
			loginResponse.success = true;
			println@Console("user:" + queryResponse.row[0].username)()
			println@Console("pass:" + queryResponse.row[0].password)()
			println@Console("bala:" + queryResponse.row[0].balance)()
			if(loginRequest.bill == undefined && 
				loginRequest.order_id == undefined && 
				loginRequest.to_user == undefined){
					print@Console("CONTROLLO TOKEN\n")()
					flag = true
			}else{
				bill = loginRequest.bill;
				to_user = loginRequest.to_user;
				order_id = loginRequest.order_id;
				// check if already payed
				query@Database("SELECT * FROM Transactions WHERE order_id='" + order_id + "' and to_user='" + to_user + "'")(query1Response)
				if(query1Response.row[0].token == null){
					loginResponse.url = "/payment-page?sid="+loginResponse.sid+"&bill="+bill+"&balance="+query1Response.row[0].balance
				} else {
					loginResponse.url = "/payed"
				}
			}
			
			idUsr = queryResponse.row[0].id
			println@Console("id:" + idUsr)()
			
			//return saldo attuale
			keepRunning = true
		}
	}
	
	while (keepRunning){
		while(!flag){
			[pay(payRequest)(payResponse){
				getRandomUUID@StringUtils()(uuid)
				
				println@Console("payment request with bill " + bill +" of user with id: " + idUsr)();  
				//get actual balance
				query@Database("SELECT balance FROM users WHERE id = "+idUsr)(query)
				balance = query.row[0].balance
				println@Console(username + " have " + balance)();
				  
				if(bill < balance){
					println@Console(username+" can pay " + bill)();

					update@Database("UPDATE users" +
							" SET balance = balance - " + bill +
							" WHERE username= '"+username+"'")(query1Response);    
							
					update@Database("INSERT INTO Transactions (token, amount, from_user, to_user, status, order_id) VALUES (:token, :amount, :from_user, :to_user, :status, :order_id)" {
							.token = uuid,
							.amount = bill,
							.from_user = idUsr,
							.to_user = to_user,
							.status = 0,
							.order_id = order_id
						}
					)(ret)
					
					payResponse.token = uuid;
					payResponse.success = true;
					keepRunning = false
				} else {
					println@Console(username + "cannot pay "+ bill)();
					payResponse.success = false
				}
			}]
			
			[logout(logoutRequest)(logoutResponse){
				println@Console(username + "logged out")();
				keepRunning = false
			}]
		}
		[verifyToken(verifyTokenRequest)(successResponse){
			order_id = verifyTokenRequest.order_id;
			token = verifyTokenRequest.token;
			print@Console("VERIFY TOKEN order_id: " + order_id + " and token " + token)();
			query@Database("SELECT * FROM Transactions WHERE token='" + token + "'and status < 2 and order_id='" + order_id + "' and to_user='" + idUsr + "'")(queryResponse);
			if(queryResponse.row[0].status == null) {
				successResponse.success = false
			} else {
				successResponse.success = true;
				update@Database("UPDATE Transactions SET status = 1 WHERE order_id='"+order_id+"' and to_user='"+idUsr+"'")(result)
			}
		}]
		[refound(refoundRequest)(successResponse){
			order_id = refoundRequest.order_id;
			print@Console("REFOUND order "+order_id)()
			query@Database("SELECT * FROM Transactions WHERE status = 1 and order_id='" + order_id + "' and to_user='" + idUsr + "'")(queryResponse);
			if(queryResponse.row[0].status == null) {
				successResponse.success = false
			} else {
				successResponse.success = true;
				update@Database("UPDATE Transactions SET status = 2 WHERE order_id='"+order_id+"' and to_user='"+idUsr+"'")(result);
				bill = queryResponse.row[0].amount;
				from_user = queryResponse.row[0].from_user;
				update@Database("UPDATE users" +
							" SET balance = balance + " + bill +
							" WHERE id= '"+from_user+"'")(query1Response)  
			}
		}]
		[confirm(confirmRequest)(successResponse){
			order_id = confirmRequest.order_id;
			print@Console("CONFIRM order "+order_id)();
			query@Database("SELECT * FROM Transactions WHERE status = 1 and order_id='" + order_id + "' and to_user='" + idUsr + "'")(queryResponse);
			if(queryResponse.row[0].status == null) {
				successResponse.success = false
			} else {
				successResponse.success = true;
				update@Database("UPDATE Transactions SET status = 3 WHERE order_id='"+order_id+"' and to_user='"+idUsr+"'")(result)
			}
		}]
	}
	
	
	
	
	
	
}
    
