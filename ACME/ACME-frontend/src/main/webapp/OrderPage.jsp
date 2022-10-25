<%--
  Created by IntelliJ IDEA.
  User: Gabriele
  Date: 23/07/2022
  Time: 19:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Order</title>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

    <script src="https://cdn.jsdelivr.net/npm/js-cookie@2/src/js.cookie.min.js"></script>

    <script src="https://cdn.jsdelivr.net/npm/vue"></script>

    <meta http-equiv="pragma" content="no-cache">
</head>
    <script>
        var totCount = 0;
        function getResInZone() {
            //get selected city
            let selectComune = document.getElementById('comune');
            let city = selectComune.options[selectComune.selectedIndex].text;
            /**SEND REQUEST**/
            let getResUrl ="http://localhost:8080/ACME-internal/getRestaurant?city=".concat(city);
            let xhr = new XMLHttpRequest();
            xhr.open("GET", getResUrl, true);
            xhr.send();

            xhr.onreadystatechange = function (){
                if (xhr.readyState === 4){
                    if (xhr.status === 200){
                        console.log("xhr done successfully");
                        let resp = xhr.responseText
                        console.log(resp);

                        document.getElementById('sceltaComune').style.display = "none";
                        document.getElementById('sceltaordine').style.display = "block";

                        /**POPULATE DROPDOWN WITH RETURNED DATA**/
                        var slotPranzo = ["12:00-12:15","12:15-12:30","12:30-12:45", "12:45-13:00",
                            "13:00-13:15","13:15-13:30","13:30-13:45", "13:45-14:00"];
                        var slotCena = ["19:00-19:15","19:15-19:30","19:30-19:45", "19:45-20:00",
                            "20:00-20:15","20:15-20:30","20:30-20:45", "22:45-22:00"]

                        var today = new Date();
                        var hour = today.getHours();
                        var minute = today.getMinutes();

                        var slot;
                        if (hour < 14) {
                            slot = slotPranzo.concat(slotCena);
                        } else {
                            slot = slotCena;
                        }

                        var timeList = document.getElementById("timeslot");

                        for (let i = 0; i < slot.length; i++) {
                            let option = document.createElement("option");
                            option.text = slot[i];
                            option.value = slot[i];
                            timeList.append(option);
                        }

                        var selected_time = timeList.options[timeList.selectedIndex].text.split("-")[0].split(":");
                        var selcted_hour = parseInt(selected_time[0]);
                        var selcted_minute = parseInt(selected_time[1]);
                        if (!(selcted_hour > hour || (selcted_hour === hour && selcted_minute > minute))) {
                            $('#inforari').html("orario selezionato errato")
                        }

                        timeList.onchange = function (){
                            $('#inforari').html(" ");
                            var selected_time = timeList.options[timeList.selectedIndex].text.split("-")[0].split(":");
                            var selcted_hour = parseInt(selected_time[0]);
                            var selcted_minute = parseInt(selected_time[1]);
                            if (!(selcted_hour > hour || (selcted_hour === hour && selcted_minute > minute))) {
                                $('#inforari').html("orario selezionato errato")
                            }
                        };

                        var respParsed = JSON.parse(resp).restaurants;
                        var resList = document.getElementById("restaurant");

                        for (let i = 0; i < respParsed.length; i++) {
                            var option = document.createElement("option");
                            option.text = respParsed[i].name;
                            option.value = respParsed[i].name.toString().concat(";" + respParsed[i].address);
                            resList.append(option);
                        }


                        var menuList = document.getElementById("menu");

                        for (let i = 0; i < respParsed[0].menu.length; i++){
                            let option = menuList.insertRow(i);
                            let c0 = option.insertCell(0);
                            c0.innerHTML = respParsed[0].menu[i].name;
                            c0.className = "col1";
                            let c1 = option.insertCell(1);
                            c1.innerHTML = respParsed[0].menu[i].price + " €";
                            c1.className = "col2";
                            let c2 = option.insertCell(2);
                            c2.className = "col3";
                            c2.innerHTML = `<button onclick='addToKart(this)' style="background-color: transparent;border: none; color: white; font-size: 28px">+</button>`;

                            //option.value = respParsed[0].menu[i].name.toString().concat(";" + respParsed[i].menu[i].price);
                            //menuList.append(option);
                        }

                        resList.onchange = function (){
                            let resSelected = resList.options[resList.selectedIndex].text;
                            let menus;
                            for(let i = 0; i < respParsed.length; i++) {
                                if(respParsed[i].name === resSelected){
                                    console.log(resSelected);
                                    menus = respParsed[i].menu;
                                }
                            }

                            menuList.innerHTML = "";
                            document.getElementById("carrello").innerHTML = "";
                            totCount = 0;
                            document.getElementById("totale").innerHTML = "";

                            for (let i = 0; i < menus.length; i++){
                                let option = menuList.insertRow(i);
                                let c0 = option.insertCell(0);
                                c0.innerHTML = menus[i].name;
                                c0.className = "col1";
                                let c1 = option.insertCell(1);
                                c1.innerHTML = menus[i].price + " €";
                                c1.className = "col2";
                                let c2 = option.insertCell(2);
                                c2.className = "col3";
                                c2.innerHTML = `<button onclick='addToKart(this)' style="background-color: transparent;border: none; color: white; font-size: 28px">+</button>`;

                            }
                        }
                    } else {
                        console.log("xhr failed");
                    }
                } else {
                    console.log("xhr processing going on");
                }
            };
            console.log("request sent succesfully");
        }

        function sendOrder() {
            var empty = false;
            //get selected restaurant
            var selectR = document.getElementById("restaurant");
            console.log(selectR);
            var choosenRname = selectR.options[selectR.selectedIndex].value.split(";")[0];
            console.log(choosenRname);
            var choosenRaddress = selectR.options[selectR.selectedIndex].value.split(";")[1];
            console.log(choosenRaddress);


            //check and get selected dishes
            var carrello = document.getElementById("carrello");
            var opts = [];
            let rowCount = carrello.rows.length;
            for (var i = 0, len = rowCount; i < len; i++) {
                let singleDish = {};
                singleDish.name = carrello.rows[i].cells[0].innerText;
                singleDish.price = carrello.rows[i].cells[1].innerText;
                console.log(singleDish.price);
                opts.push(singleDish);
            }
            console.log(opts);


            var today = new Date();
            var hour = today.getHours();
            var minute = today.getMinutes();
            var orari = document.getElementById("timeslot");
            var deliveryTime;

            var selected_time = orari.options[orari.selectedIndex].text.split("-")[0].split(":");
            var selcted_hour = parseInt(selected_time[0]);
            var selcted_minute = parseInt(selected_time[1]);
            deliveryTime = orari.options[orari.selectedIndex].text.split("-")[0];

            if (!(selcted_hour > hour || (selcted_hour === hour && selcted_minute > minute))) {
                $('#inforari').html("orario selezionato errato");
                empty = true;
            } else {
                $('#inforari').html(" ")
            }

            if (!document.getElementById("indirizzo").value) {
                empty = true;
                $('#infoind').html("Inserisci l'indirizzo di consegna");
            }

            if (!empty) {
                document.getElementById("sceltaordine").style.display = "none";
                document.getElementById("send").style.display = "block";

                console.log("Manda ordine");

                var order =
                    {
                        "restaurant": choosenRname,
                        "dishes": opts,
                        "oraCons": deliveryTime,
                        "indRisto": choosenRaddress,
                        "indCliente": document.getElementById("indirizzo").value
                    };

                var restaurant_url = "http://localhost:8080/ACME-internal/sendOrder";
                var xhr = new XMLHttpRequest();
                xhr.withCredentials = true;
                xhr.open("POST", restaurant_url, true);
                xhr.setRequestHeader("Content-type", "application/json");
                var params = JSON.stringify(order);
                console.log(params);
                xhr.send(params);
                xhr.onreadystatechange = function () {
                    console.log(xhr.responseText);
                    if (xhr.readyState === 4) {
                        if (xhr.status === 200) {
                            let resp = xhr.responseText
                            var respParsed = JSON.parse(resp).info;
                               // var bank = JSON.parse(resp).bank_url;
                           // var price = JSON.parse(resp).total_price;
                            if(respParsed == "out of time"){
                               alert("il tempo per completare l'ordine è scaduto");
                               window.location="http://localhost:8080/ACMEat/ClientServlet";
                            }
                            if(respParsed == "abortRest"){
                                console.log(respParsed);
                                document.getElementById("send").style.display = "none";
                                document.getElementById("abortRe").style.display = "block";
                            }
                            else
                            if(respParsed == "abortRider"){
                                console.log(respParsed);
                                document.getElementById("send").style.display = "none";
                                document.getElementById("abortRi").style.display = "block";
                            }
                            else
                            if(respParsed == "no"){
                                console.log(respParsed);
                                document.getElementById("send").style.display = "none";
                                document.getElementById("timeEx").style.display = "block";
                            }
                            else
                                if(respParsed == "go"){
                                console.log(resp);
                                var callback_url = encodeURIComponent("http://localhost:8080/ACMEat/ClientAfterPayment");
                                var bank_url = bank + "home/" + "price/" + price + "/callback_url/" + callback_url;
                                window.location.assign(bank_url);
                                //window.location = "http://localhost:8080/ACMEat/ClientAfterPayment";
                            }
                        } else {
                            console.log("NO");
                        }
                    }
                }
            }


        }

        function goHome() {
            window.location = "http://localhost:8080/ACMEat/ClientServlet";
        }

        function addToKart(ele) {
            var table = document.getElementById("carrello");
            let rowCount = table.rows.length;
            var tot = document.getElementById("totale");
            price = +ele.parentNode.parentNode.cells[1].innerText.replace(/[^\d.]/g, "");
            totCount = totCount + price;

            tot.innerHTML = "<label>TOT "+totCount+" €</label>"

            let row = table.insertRow(rowCount);
            let c0 = row.insertCell(0);
            c0.className = "col1";
            c0.innerHTML = ele.parentNode.parentNode.cells[0].innerText;
            let c1 = row.insertCell(1);
            c1.className = "col2";
            c1.innerHTML = ele.parentNode.parentNode.cells[1].innerText;
            let c2 = row.insertCell(2);
            c2.className = "col4";
            c2.innerHTML = `<button onclick='deleteRow(this)' style="background-color: transparent;border: none;"><img src="icon/delete.svg" height="40"></button>`;
        }

        function deleteRow(ele){
            let rowIndex = ele.parentNode.parentNode.rowIndex;
            var tot = document.getElementById("totale");
            price = +ele.parentNode.parentNode.cells[1].innerText.replace(/[^\d.]/g, "");
            totCount = totCount - price;

            tot.innerHTML = "<label>TOT "+totCount+" €</label>"
            ele.parentNode.parentNode.remove();
        }

    </script>
    <link rel="stylesheet" href="clientPage.css">
<body>
<div class="line">

    <h1>
        <button onclick="location.href='../ACMEat'" style="background-color: transparent;border: none;">
            <img src="icon/homeClient.svg" height="40">
        </button>
        PAGINA CLIENTI
    </h1>
</div>



<div id="sceltaComune" class="main_card">
    <h2>Seleziona comune </h2>
    <div class="line">
        <label for="comune">Città</label>
        <select name="comune" id="comune">
            <option value="Cagliari" selected>Cagliari</option>
            <option value="Trento">Trento</option>
            <option value="Mantova">Mantova</option>
        </select>
    </div>
    <button type="submit" onclick="getResInZone()">Cerca</button>
</div>

<div id="sceltaordine"  hidden="true">
    <div class="main_card">
        <h2>Seleziona ristorante</h2>
        <div class="line">
            <label for="restaurant">Ristorante</label>
            <select name="restaurant" id="restaurant"></select>
        </div>
    </div>

    <div class="main_card">
        <h2>Seleziona piatti</h2>
        <table name="menu" id="menu">

        </table>
    </div>

    <div class="main_card">
        <h2>Carrello</h2>
        <table name="carrello" id="carrello"></table>
        <div id="totale" class="line" style="text-align: right"> </div>
    </div>

    <div class="main_card">
        <h2>Orario e luogo</h2>
        <div class="line">
            <label for="timeslot">Orario</label>
            <select name="timeslot" id="timeslot"></select>
            <div id="inforari" style="color:rgb(226, 43, 43)"></div>
        </div>
        <div class="line">
            <label for="indirizzo">Indirizzo</label>
            <input type="text" id="indirizzo" name="indirizzo" value=""><br><br>
            <div id="infoind" style="color:rgb(226, 43, 43)"></div>
        </div>
        <button type="submit" onclick="sendOrder()">SEND ORDER</button>
    </div>

</div>
<div id="send" class="main_card" hidden="true">
    <h2>ORDINE IN ELABORAZIONE</h2>
    <label>
        Stiamo verificando la fattibilità del tuo ordine,
        verrai reindirizzato alla pagina del pagamento una volta terminati i controlli.
    </label>
</div>
<div id="timeEx" hidden="true">
    <h2>TEMPO PER EFFETTUARE L'ORDINE SCADUTO</h2>
    <div class="line">
        <label>
            Il tuo ordine non può essere eseguito, ci scusiamo per il disagio.
        </label>
    </div>
    <button type="submit" onclick="goHome()">nuovo ordine</button>
</div>
<div id="abortRe" hidden="true">
    <h2>RISTORANTE NON DISPONIBILE</h2>
    <div class="line">
        <label>
            Il tuo ordine non può essere eseguito, ci scusiamo per il disagio.
        </label>
    </div>
    <button type="submit" onclick="goHome()">nuovo ordine</button>
</div>
<div id="abortRi" hidden="true">
    <h2>NESSUN RIDER DISPONIBILE</h2>
    <div class="line">
        <label>
            Il tuo ordine non può essere eseguito, ci scusiamo per il disagio.
        </label>
    </div>
    <button type="submit" onclick="goHome()">nuovo ordine</button>
</div>


</body>
</html>
