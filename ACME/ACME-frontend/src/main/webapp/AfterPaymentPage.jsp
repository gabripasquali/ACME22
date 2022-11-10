<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Client</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script type='text/JavaScript'>
        function goHome() {
            window.location = "http://localhost:8080/ACMEat/ClientServlet";
        }

        function cancelOrder() {

            var url = "http://localhost:8080/ACME-internal/abort";
            var xhr = new XMLHttpRequest();
            xhr.open("PUT", url, true);
            xhr.send();

            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        let resp = xhr.responseText;
                        //TODO: mettere messaggio corretto nella cancellazione
                        var respParsed = JSON.parse(resp);
                        console.log(resp);
                        //var message;
                        if (respParsed === "ok") {
                            document.getElementById("cancel-button").style.display = "none";
                            document.getElementById("home").style.display = "block"; 
                        } else 
                            if(respParsed === "no"){
                                document.getElementById("cancel-button").style.display = "none";
                                document.getElementById("noAbort").style.display = "block"; 
                        }
                       
                    }
                }
            }
            console.log("request sent succesfully");
        }

        function sendToken() {

            var url = "http://localhost:8080/ACME-internal/sendToken?token=" + "<%=request.getParameter("token") %>";
            var xhr = new XMLHttpRequest();
            xhr.open("PUT", url, true);
            xhr.send();

            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        var resp = xhr.responseText;
                        var respParsed = JSON.parse(resp);
                        if (respParsed.result.status === "success") {
                            $('#first').hide();
                            $('#token-success').show();
                        } else {
                            $('#first').hide();
                            $('#token-failure').show();
                        }
                    }
                }
            };
        }
    </script>
</head>
<body>
<div id="token-success">
   <!-- <div id="abort">
        <h3>Puoi cancellere il tuo ordine entro un'ora dall'orario di consegna</h3>
    </div> -->
    <div id="cancel-button" >
        <h3>Puoi cancellere il tuo ordine entro un'ora dall'orario di consegna</h3>
        <br><br>
        <button type="submit" onclick="cancelOrder()">CANCELLA ORDINE</button>
    </div>
    <div id="noAbort" hidden="true">
        <h3>IMPOSSIBILE ANNULLARE L'ORDINE</h3>
        Limite di tempo per annullare l'ordine scaduto!
        Il tuo ordine sta per arrivare!
    </div>
    <div id="home" hidden="true">
        <h3>ORDINE ANNULLATO CON SUCCESSO</h3>
        Clicca sul tasto HOME se vuoi effettuare un nuovo ordine!
        <br><br>
        <button type="submit" onclick="goHome()">HOME</button>
    </div>

</div>


<div id="token-failure" hidden="true">Pagamento fallito<input type="submit" value="Ritorna alla homepage"
                                                              onclick="goHome()"></div>
</body>
</html>
