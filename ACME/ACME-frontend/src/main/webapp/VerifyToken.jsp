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
        function goHome() {
            window.location = "ClientServlet";
        }
        function verifyToken() {
            //call camunda servlet
            let body = {
                "token": document.getElementById("token").value,
                "instanceId": "<%=session.getAttribute("instanceId")%>"
            };
            var url = "verifyToken"
            var xhr = new XMLHttpRequest();
            xhr.open("POST", url, true);
            xhr.setRequestHeader("Content-type", "application/json");
            var params = JSON.stringify(body);
            xhr.send(params);
            xhr.onreadystatechange = function (){
                console.log(xhr.responseText);
                if(xhr.status === 200){
                    let resp = xhr.responseText;
                    if(resp == "true" || resp == true){
                        window.location = "ClientAfterPayment";
                    }
                    else {
                        document.getElementById("tokenDiv").style.display = "none";
                        document.getElementById("home").style.display = "block";
                    }
                }
            }

        }
    </script>
    <link rel="stylesheet" href="clientPage.css">
</head>
<body>
    <h1>CONFERMA ACQUISTO</h1>
    <div id="tokenDiv"  class="main_card">
        <h2>INSERISCI TOKEN</h2>
        <label>
            Inserisci il token che la banca ti ha rilasciato a fronte
            del pagamento per poter confermare il pagamento
        </label>
        <div class="line">
            <label for="token">Token</label>
            <input type="text" id="token">
        </div><br><br>
        <button type="submit" onclick="verifyToken()">VERIFICA</button>
    </div>

    <div id="home" hidden="true" class="main_card">
        <h2>IMPOSSIBILE VERIFICARE ORDINE</h2>
        <div class="line">
            <label>
                Se hai effettuato il pagamento ti verrà rimborsato<br>
                Clicca sul tasto HOME se vuoi effettuare un nuovo ordine!
            </label>
        </div>

        <button type="submit" onclick="goHome()">HOME</button>
    </div>


</body>
</html>
