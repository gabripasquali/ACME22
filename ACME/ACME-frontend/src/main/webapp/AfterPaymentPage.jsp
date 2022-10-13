<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Client</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script type='text/JavaScript'>
        function goHome() {
            window.location = "http://localhost:8080/acmeat-frontend/client-home";
        }

        function cancelOrder() {

            var url = "http://localhost:8080/acmeat-ws/abort";
            var xhr = new XMLHttpRequest();
            xhr.open("PUT", url, true);
            xhr.send();

            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        var resp = xhr.responseText;
                        $('#cancel-button').hide();
                        //TODO: mettere messaggio corretto nella cancellazione
                        var respParsed = JSON.parse(resp);
                        var message;
                        if (respParsed.result.message === "") {
                            message = "Il tuo ordine e stato cancellato";
                        } else {
                            message = respParsed.result.message;
                        }
                        $('#canc').html(message);
                    }
                }
            };
            console.log("request sent succesfully");
        }

        function sendToken() {

            var url = "http://localhost:8080/acmeat-ws/confirm?token=" + "<%=request.getParameter("token") %>";
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
<div id="first">Hai completato il pagamento, ora confermalo ad Acme:
    <input type="submit" value="Conferma pagamento" onclick="sendToken()"></div>


<div id="token-success" hidden="true"><input id="cancel-button" type="submit" value="Cancella ordine"
                                             onclick="cancelOrder()">
    <div id="canc"></div>
</div>


<div id="token-failure" hidden="true">Pagamento fallito<input type="submit" value="Ritorna alla homepage"
                                                              onclick="goHome()"></div>
</body>
</html>
