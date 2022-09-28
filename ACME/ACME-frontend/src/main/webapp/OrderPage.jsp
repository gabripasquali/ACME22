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
                        var slot = ["12:00-12:15","12:15-12:30","12:30-12:45", "12:45-13:00",
                            "13:00-13:15","13:15-13:30","13:30-13:45", "13:45-14:00",
                            "19:00-19:15","19:15-19:30","19:30-19:45", "19:45-20:00",
                            "20:00-20:15","20:15-13:30","20:30-20:45", "20:45-21:00"]
                        var timeList = document.getElementById("timeslot");
                        for (let i = 0; i < slot.length; i++) {
                            let option = document.createElement("option");
                            option.text = slot[i];
                            option.value = slot[i];
                            timeList.append(option);
                        }
                        //controllo su orario selezionato
                        
    

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
                            let option = document.createElement("option");
                            option.text = respParsed[0].menu[i].name.concat(" ( " + respParsed[0].menu[i].price + " €)");
                            option.value = respParsed[0].menu[i].name.toString().concat(";" + respParsed[i].menu[i].price);
                            menuList.append(option);
                        }

                        resList.onchange = function (){
                            let resSelected = resList.options[resList.selectedIndex].text;
                            let menus;
                            for(let i = 0; i < respParsed.length; i++) {
                                if(respParsed[i].name === resSelected){
                                    console.log(resSelected);
                                    menus = respParsed[i].menu;
                                } else{
                                    console.log("false"+respParsed[i].name)
                                }
                            }
                            var menuList = document.getElementById("menu");
                            menuList.innerText = ""
                            for (let i = 0; i < menus.length; i++){
                                let option = document.createElement("option");
                                option.text = menus[i].name.concat(" ( " + menus[i].price + " €)");
                                option.value = menus[i].price.concat(";" + menus[i].price);
                                console.log(menus[i]);
                                menuList.append(option);
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
                //call servlet that start camunda engine
                document.getElementById("sceltaordine").style.display = "none";
                document.getElementById("send").style.display = "block";

         
                
                     console.log("Manda ordine");

        //get selected restaurant
        var selectR = document.getElementById("restaurant");
        console.log(selectR);
        var choosenRname = selectR.options[selectR.selectedIndex].value.split(";")[0];
        console.log(choosenRname);
        var choosenRaddress = selectR.options[selectR.selectedIndex].value.split(";")[1];
        console.log(choosenRaddress);


        //check and get selected dishes
   
            var opts = [], opt;
            var selectD = document.getElementById("menu");
            for (var i = 0, len = selectD.options.length; i < len; i++) {
                opt = selectD.options[i];
                // check if selected
                if (opt.selected) {
                    var singleDish = {};
                    singleDish.name = opt.value.split(",")[0];
                    singleDish.price = opt.value.split(",")[1];
                    opts.push(singleDish);
                }
            }
        var today = new Date();
        var hour = today.getHours();
        var minute = today.getMinutes();
        var orari = document.getElementById("timeslot");
        var deliveryTime;

        var selected_time = orari.options[orari.selectedIndex].text.split("-")[0].split(":");
        var selcted_hour = parseInt(selected_time[0]);
        var selcted_minute = parseInt(selected_time[1]);
        deliveryTime = orari.options[orari.selectedIndex].text.split("-")[0];
     
		
	
  
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
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                    	let resp = xhr.responseText
                        var respParsed = JSON.parse(resp).info;
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
                                if(respParsed == "go"){
                                    console.log(resp);
                                    window.location = "http://localhost:8080/ACMEat/ClientAfterPayment";
                                }
                        } else {
                           console.log("NO");
                        }
                    }
                }
     
            }
        //
        function goHome() {
            window.location = "http://localhost:8080/ACMEat/ClientServlet";
        }

    </script>

<body>
<h1>PAGINA CLIENTI</h1>

<div id="sceltaComune">
    <h2>Seleziona il tuo comune: </h2>
    <select name="comune" id="comune">
        <option value="Cagliari">Cagliari</option>
        <option value="Trento">Trento</option>
        <option value="Mantova">Mantova</option>
    </select>
    
    <button type="submit" onclick="getResInZone()">cerca ristoranti</button>
</div>

<div id="sceltaordine" hidden="true">
    <h2>Scelta ordine</h2>
<div id="info2" style="color:red"></div>
    
    
    <label for="restaurant">Ristorante</label>
    <select name="restaurant" id="restaurant"></select><br>
    <label for="menu">Menù</label>
    <select name="menu" id="menu"></select><br>
    <label for="timeslot">Orario di consegna</label>
    <select name="timeslot" id="timeslot"></select><br>
    Indirizzo di consegna:
    <input type="text" id="indirizzo" name="indirizzo" value=""><br><br>
   
    <button type="submit" onclick="sendOrder()">SEND ORDER</button>
</div>
<div id="send" hidden="true">
    <h2>ORDINE IN ELABORAZIONE</h2>
    stiamo verificando la fattibilità del tuo ordine,
    verrai reindirizzato alla pagina del pagamento una volta terminati i controlli.
</div>
<div id="abortRe" hidden="true">
    <h3>RISTORANTE NON DISPONIBILE</h3>
    Il tuo ordine non può essere eseguito, ci scusiamo per il disagio.
    <br><br>
    <button type="submit" onclick="goHome()">HOME</button>
</div>
<div id="abortRi" hidden="true">
    <h3>NESSUN RIDER DISPONIBILE</h3>
    Il tuo ordine non può essere eseguito, ci scusiamo per il disagio.
    <br><br>
    <button type="submit" onclick="goHome()">HOME</button>
</div>


</body>
</html>
