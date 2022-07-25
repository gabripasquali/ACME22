include "console.iol"
include "interface.iol"
include "math.iol"

inputPort MyInput {
    Location: "socket://localhost:8000"
    Protocol: soap {
        .wsdl = "service.wsdl";
        .wsdl.port = "BankPort"
    }
    Interfaces: interface
}

execution{ concurrent }

cset{
    sid: 
        getToken.sid 
        verifyToken.sid 
        refound.sid
}

init{
    println@Console("Bank server is running...")()
    keepRunning = true
}

main{
    while(keepRunning){
        /********************************************
        *        GET TOKEN
        ********************************************/
        [getToken(getTokenRequest)(getTokenResponse){
            getTokenReponse.sid = csets.sid = new
            random@Math()(randomResult);
            if (randomResult<0.5){
                getTokenResponse.status="success";
                global.users.(getTokenResponse.sid).result = true;
                global.users.(getTokenRequest.sid).isRefounded = false;
                println@Console("Correct token")
            }else{
                getTokenResponse.status="failure";
                global.users.( getTokenResponse.sid ).result = false;
                global.users.(getTokenRequest.sid).isRefounded = true;
                println@Console("Incorrect token")
            };
        }]{
            println@Console("Operazione GetToken completata")()
        }

        /********************************************
        *        VERIFY TOKEN
        ********************************************/
        [verifyToken(verifyTokenRequest)(verifyTokenResponse){
            verifyTokenResponse.success = global.users.(verifyTokenRequest.sid).result;
            if(verifyTokenResponse.success == true){
                println@Console("Token valido")()
            }else{
                println@Console("Token non valido")()
            };
        }]{
            println@Console("Operazione VerifyToken completata")()
        }

        /********************************************
        *        REFOUND
        ********************************************/
        [refound(refoundRequest)(refoundResponse){
            global.users.(refoundRequest.sid).result = false;
            if (!global.users.(refoundRequest.sid).isRefounded){
                global.users.(refoundRequest.sid).isRefounded = true;
                global.users.(refoundRequest.sid).result = true;
            };
            refoundResponse.success=global.users.(refoundRequest.sid).result;
            println@Console("Rimborso effettuato")()
        }]{
            println@Console("Operazione Refound completata")()
        }        
    }
    
}