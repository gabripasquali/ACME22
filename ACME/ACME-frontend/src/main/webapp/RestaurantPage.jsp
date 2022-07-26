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
    <title>ACMEat - Ristoranti</title>
    <script src="https://code.jquery.com/jquery-2.2.1.min.js">

    </script>
    <script>
        var dishes = [];

        function addRow() {
            let nome = document.getElementById("nomePiatto").value;
            let price = document.getElementById("prezzoPiatto").value;
            let rowCount = document.getElementById("new-menu").rows.length;
            let row = document.getElementById("new-menu").insertRow(rowCount);
            let c0 = row.insertCell(0);
            c0.innerHTML = nome;
            c0.className = "col1";
            let c1 = row.insertCell(1);
            c1.innerText = price;
            c1.className="col2";
            let c2 = row.insertCell(2);
            c2.className="col3";
            c2.innerHTML = `<button onclick='deleteRow(this)' style="background-color: transparent;border: none;"><img src="icon/delete.svg" height="40"></button>`;
            dishes.push({"name" : nome, "price" : price});

            console.log(dishes);
        }

    function deleteRow(ele){
        let rowPos = ele.parentNode.parentNode.rowIndex;
        ele.parentNode.parentNode.remove();
        let copy = [];
        for(let i = 0; i<dishes.length; i++){
            if(i!=rowPos){
                copy.push(dishes[i]);
            }
        }
        dishes = copy;
        console.log(dishes);

    }

    function changeAvailability(){
            var ele = document.getElementsByName("availability");
            var availability;
            for (let i = 0; i < ele.length; i++){
                if(ele[i].checked)
                    availability = ele[i].value;
            }
            let requestBody = {
                "name" : document.getElementById("restaurant").value.toString(),
                "disp" : availability.toString()
            };
            console.log(requestBody);

            let availabilityUrl ="changeAvailability";
            let xhr = new XMLHttpRequest();
            xhr.open("POST", availabilityUrl, true);
            xhr.send(JSON.stringify(requestBody));

            xhr.onreadystatechange = function (){
                if (xhr.readyState === XMLHttpRequest.DONE){
                    const status = xhr.status;
                    let resp = xhr.responseText;
                    console.log(resp);
                    if(status === 0 || (status >= 200 && status < 400)){
                        if(resp == "true" || resp == true)
                            alert("la disponibilità è stata aggiornata");
                        else
                            alert("non è stato possibile aggiornare il menù. riprova entro le 10 del mattino");
                    } else {
                        alert("server error")
                    }
                }
            }
        }

        function updateMenu(){
            let updateUrl ="changeMenu";
            //read new plate
            let requestBody = {
                "name" : document.getElementById("ristorante").value.toString(),
                "menu" : dishes
            };
            console.log(requestBody);

            let xhr = new XMLHttpRequest();
            xhr.open("POST", updateUrl, true);
            xhr.send(JSON.stringify(requestBody));

            xhr.onreadystatechange = function (){
                if (xhr.readyState === XMLHttpRequest.DONE){
                    const status = xhr.status;
                    let resp = xhr.responseText;
                    console.log(resp);
                    if(status === 0 || (status >= 200 && status < 400)){
                        if(resp == "true" || resp == true)
                            alert("il menu è stato aggiornato");
                        else
                            alert("non è stato possibile aggiornare il menù. riprova entro le 10 del mattino");
                    } else {
                        alert("server error")
                    }
                }
            }
        }

        function getResMenu(){
            let getMenuUrl = "getMenu"
            let res = document.getElementById("ristorante").value.toString();
            if(res != "default"){
                let xhr = new XMLHttpRequest();
                xhr.open("GET",getMenuUrl+"?res="+res);
                xhr.send();
                xhr.onreadystatechange = function (){
                    if(xhr.readyState === XMLHttpRequest.DONE){
                        const status = xhr.status;
                        console.log("menu response"+xhr.responseText);
                        if(status === 0 || (status>=200 && status< 400)){
                            var menu = JSON.parse(xhr.responseText);
                            let table = document.getElementById("new-menu");
                            table.innerHTML = "";
                            dishes = [];
                            for(let i = 0; i < menu.length; i++){
                                let row = table.insertRow(i);
                                let c0 = row.insertCell(0)
                                c0.innerHTML = menu[i].name;
                                c0.className = "col1"
                                let c1 = row.insertCell(1)
                                c1.innerHTML = menu[i].price;
                                c1.className = "col2";
                                let c2 = row.insertCell(2);
                                c2.className="col3";
                                c2.innerHTML = `<button onclick='deleteRow(this)' style="background-color: transparent;border: none;"><img src="icon/delete.svg" height="40"></button>`;
                                dishes.push(menu[i])
                            }
                        }
                    }
                }
            }
        }
    </script>
    <link rel="stylesheet" href="resPage.css">
</head>
<body>
<h1>
    <button onclick="location.href='../ACMEat'" style="background-color: transparent;border: none;">
        <img src="icon/homeRes.svg" height="40">
    </button>
    PAGINA RISTORANTI
</h1>
<div class="main_card">
    <h2>Aggiorna disponibilità</h2>
    <div class="line">
        <label for="restaurant">Nome ristorante</label>
        <select id="restaurant" name="ristorante">
            <option value="Vegetale" selected>Vegetale</option>
            <option value="DeCarlo">DeCarlo</option>
            <option value="Paradiso">Paradiso</option>
            <option value="Sushino">Sushino</option>
            <option value="Tramonto">Tramonto</option>
            <option value="YinDyan">YinDyan</option>
        </select>
    </div>

    <div class="line">
        <label>Disponibilità</label>
        <input type="radio" id="avTrue" name="availability" value="true" style="margin-left: 142px" checked>
        <label for="avTrue">Disponibile</label>
        <input type="radio" id="avFalse" name="availability" value="true">
        <label for="avFalse">Non disponibile</label>
    </div>
    <button type="submit"  onclick="changeAvailability()">Invia</button>
</div>


<%--insert new plate--%>
<div class="main_card">
    <h2>Aggiorna menù</h2>
    <div class="line">
        <label for="ristorante">Nome ristorante</label>
        <select id="ristorante" onchange="getResMenu()" name="ristorante" >
            <option value="default" selected></option>
            <option value="Vegetale" >Vegetale</option>
            <option value="DeCarlo">DeCarlo</option>
            <option value="Paradiso">Paradiso</option>
            <option value="Sushino">Sushino</option>
            <option value="Tramonto">Tramonto</option>
            <option value="YinDyan">YinDyan</option>
        </select>
    </div>
    <div class="line">
        <form id="newDish" action="" style="margin: 0" >
            <input type="text" id="nomePiatto" name="nome piatto" placeholder="Nome" required>
            <input type="number" id="prezzoPiatto" step="0.01" name = "prezzo" placeholder="Prezzo" required>
            <input class="submit" type="submit" value="+" style="float: right; width: 48px;" >
        </form>
    </div>


    <script>
        $(function (){
            $('#newDish').on('submit',function (e){
                e.preventDefault();
                addRow();
            })
        })</script>


    <table id="new-menu">

    </table>

    <button type="submit"  onclick="updateMenu()">Invia</button>
</div>

</body>
</html>
