include "console.iol"
include "interface.iol"
include "math.iol"

inputPort Bank {
    Location: "socket://localhost:3002"
    Protocol: soap {
        .wsdl = "BankService.wsdl";
        .wsdl.port = "Bank"
    }
    Interfaces: interface
}

execution{ concurrent }

cset{
    sid: 
       getTokenResponse.sid
        verifyTokenRequest.sid
  refoundRequest.sid
}

init{
    println@Console("Bank server is running...")()
}

main{
        [getToken(getTokenRequest)(getTokenResponse){
            getTokenReponse.sid = csets.sid = new
            random@Math()(randomResult);
            if (randomResult<0.5){
                getTokenResponse.status="success";
                global.users.(getTokenResponse.sid).result = true;
                global.users.(getTokenRequest.sid).isRefounded = false
                //println@Console("Correct token")
            }else{
                getTokenResponse.status="failure";
                global.users.( getTokenResponse.sid ).result = false;
                global.users.(getTokenRequest.sid).isRefounded = true
                //println@Console("Incorrect token")
            }
        }]{
            println@Console("Operazione GetToken completata")()
        }

        [verifyToken(verifyTokenRequest)(verifyTokenResponse){
            verifyTokenResponse.success = global.users.(verifyTokenRequest.sid).result;
            if(verifyTokenResponse.success == true){
                println@Console("Token valido")()
            }else{
                println@Console("Token non valido")()
            }
        }]{
            println@Console("Operazione VerifyToken completata")()
        }

        [refound(refoundRequest)(refoundResponse){
            global.users.(refoundRequest.sid).result = false;
            if (!global.users.(refoundRequest.sid).isRefounded){
                global.users.(refoundRequest.sid).isRefounded = true;
                global.users.(refoundRequest.sid).result = true
            }
            refoundResponse.success=global.users.(refoundRequest.sid).result;
            println@Console("Rimborso effettuato")()
        }]{
            println@Console("Operazione Refound completata")()
        }        
}
    