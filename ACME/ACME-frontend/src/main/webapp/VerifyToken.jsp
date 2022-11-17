<%--
  Created by IntelliJ IDEA.
  User: Gabriele
  Date: 08/11/2022
  Time: 17:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Conferma pagamento</title>
    <script>
        function verifyToken() {
            //call camunda servlet
            let body = {"token": document.getElementById("token").value};
            var url = "verifyToken"
            var xhr = new XMLHttpRequest();
            xhr.open("POST", url, true);
            xhr.setRequestHeader("Content-type", "application/json");
            var params = JSON.stringify(body);
            xhr.send(params);
            xhr.onreadystatechange = function (){
                console.log(xhr.responseText);
                if (xhr.readyState === 4 ){
                    if(xhr.status === 200){
                        let resp = xhr.responseText;
                        if(resp){
                            window.location = "ClientAfterPayment";
                        }
                        //check response
                        //true go to clientAfter payment
                        //else try again(?)

                    }
                }
            }
        }
    </script>
</head>
<body>
    <label for="token">Token rilasciato dalla banca:</label>
    <input type="text" id="token">
    <button onclick="verifyToken()">Verifica</button>

</body>
</html>
