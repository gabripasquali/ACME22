<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Client</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script type='text/JavaScript'>
        function goHome() {
            window.location = "ClientServlet";
        }

        function cancelOrder() {
            let param = {
              "instanceId" : "<%=session.getAttribute("instanceId")%>"
            };
            var url = "abort";
            var xhr = new XMLHttpRequest();
            xhr.open("PUT", url, true);
            xhr.send(JSON.stringify(param));

            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        let resp = xhr.responseText;
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


    </script>
    <link rel="stylesheet" href="clientPage.css">
</head>
<body>
<div class="line">

    <h1>
        ORDINE IN ELABORAZIONE
    </h1>
</div>
    <div id="cancel-button" class="main_card">
        <h2>VUOI ANNULLARE L'ORDINE?</h2>
        <div class="line">
            <label>Puoi cancellere il tuo ordine entro un'ora dall'orario di consegna</label>
        </div>
        <button type="submit" onclick="cancelOrder()">CANCELLA ORDINE</button>
    </div>
    <div id="noAbort" hidden="true" class="main_card">
        <h2>IMPOSSIBILE ANNULLARE L'ORDINE</h2>
        <label>
            Limite di tempo per annullare l'ordine scaduto!
        Il tuo ordine sta per arrivare!
        </label>
    </div>
    <div id="home" hidden="true" class="main_card">
        <h2>ORDINE ANNULLATO CON SUCCESSO</h2>
        <div class="line">
            <label>
                Clicca sul tasto HOME se vuoi effettuare un nuovo ordine!
            </label>
        </div>

        <button type="submit" onclick="goHome()">HOME</button>
    </div>

</body>
</html>
