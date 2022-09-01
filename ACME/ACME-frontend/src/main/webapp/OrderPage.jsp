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
    <script>
        function sendOrder() {
            //call servlet that start camunda engine
            document.getElementById("sceltaordine").style.display = "none";
            document.getElementById("loading").style.display = "block";

        }

        function getResInZone() {
            //get selected city
            let selectComune = document.getElementById('comune');
            let city = selectComune.options[selectComune.selectedIndex].text;
            console.log(city);
            /**SEND REQUEST**/
            let getResUrl ="getRestaurant?city=".concat(city);
            let xhr = new XMLHttpRequest();
            xhr.open("GET", getResUrl, true);
            xhr.send();

            xhr.onreadystatechange = function (){
                if (xhr.readyState === 4){
                    if (xhr.status === 200){
                        let resp = xhr.responseText
                        console.log("RESPONSE ON GetRes REQUEST"+resp);


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

                        var resParsed = JSON.parse(resp);
                        var resList = document.getElementById("restaurant");

                        for (let i = 0; i < resParsed.length; i++){
                            let option = document.createElement("option");
                            option.text = resParsed[i].name;
                            option.value = resParsed[i].name.toString();
                            resList.append(option);
                        }

                        var menuList = document.getElementById("menu");
                        for (let i = 0; i < resParsed[0].menu.length; i++){
                            let option = document.createElement("option");
                            option.text = resParsed[0].menu[i];
                            option.value = resParsed[0].menu[i];
                            menuList.append(option);
                        }

                        resList.onchange = function (){
                            let resSelected = resList.options[resList.selectedIndex].text;
                            let menus;
                            for(let i = 0; i < resParsed.length; i++) {
                                if(resParsed[i].name === resSelected){
                                    console.log(resSelected);
                                    menus = resParsed[i].menu;
                                } else{
                                    console.log("false"+resParsed[i].name)
                                }
                            }
                            var menuList = document.getElementById("menu");
                            menuList.innerText = ""
                            for (let i = 0; i < menus.length; i++){
                                let option = document.createElement("option");
                                option.text = menus[i];
                                option.value = menus[i];
                                console.log(menus[i]);
                                menuList.append(option);
                            }
                        }

                    } else {
                        console.error("request failed");
                    }
                } else {
                    console.log("request processing");
                }
            }


            }
    </script>
</head>
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
    <label for="restaurant">Ristorante</label>
    <select name="restaurant" id="restaurant"></select><br>
    <label for="menu">Menù</label>
    <select name="menu" id="menu"></select><br>
    <label for="timeslot">Orario di consegna</label>
    <select name="timeslot" id="timeslot"></select><br><br>
    <button type="submit" onclick="sendOrder()">SEND ORDER</button>
</div>

<div id="loading" hidden="true">
    <h2>ORDINE IN ELABORAZIONE</h2>
    stiamo verificando la fattibilità del tuo ordine,
    verrai reindirizzato alla pagina del pagamento una volta terminati i controlli
</div>

</body>
</html>
