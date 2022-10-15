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
        let c1 = row.insertCell(1);
        c1.innerText = price;
        let c2 = row.insertCell(2);
        c2.innerHTML = `<button onclick='deleteRow(this)'>delete</button>`;
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
            let availability = document.getElementById("availability").value;
            let requestBody = {
                "name" : document.getElementById("restaurant").value.toString(),
                "disp" : availability.toString()
            };
            console.log(requestBody);

            let availabilityUrl ="http://localhost:8080/ACME-internal/changeAvailability";
            let xhr = new XMLHttpRequest();
            xhr.open("POST", availabilityUrl, true);
            xhr.send(JSON.stringify(requestBody));

            xhr.onreadystatechange = function (){
                if (xhr.readyState === XMLHttpRequest.DONE){
                    const status = xhr.status;
                    let resp = xhr.responseText;
                    console.log(resp);
                    if(status === 0 || (status >= 200 && status < 400)){
                        alert("la disponibilità è stata aggiornata");
                    } else {
                        alert("non è stato possibile aggiornare la disponibilità. riprova entro le 10 del mattino")
                    }
                }
            }
        }

        function updateMenu(){
            let updateUrl ="http://localhost:8080/ACME-internal/changeMenu";
            //read new plate
            let requestBody = {
                "name" : document.getElementById("restaurant").value.toString(),
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
                        alert("il menu è stato aggiornato");
                    } else {
                        alert("non è stato possibile aggiornare il menù. riprova entro le 10 del mattino")
                    }
                }
            }
        }
    </script>
    <link rel="stylesheet" href="resPage.css">
</head>
<body>
<h1> PAGINA RISTORANTI</h1>
<div>
    <h2>MODIFICA DISPONIBILITA'</h2>
    <p>
        <label for="restaurant">ristorante:</label>
        <select id="restaurant" name="ristorante">
            <option value="Vegetale" selected>Vegetale</option>
            <option value="DeCarlo">DeCarlo</option>
            <option value="Paradiso">Paradiso</option>
            <option value="Sushino">Sushino</option>
            <option value="Tramonto">Tramonto</option>
            <option value="YinDyan">YinDyan</option>
        </select>
    </p>


    <p>
        <label>Disponibilità</label>
        <input type="radio" id="avTrue" name="availability" value="true">
        <label for="avTrue">Disponibile</label>
        <input type="radio" id="avFalse" name="availability" value="true">
        <label for="avFalse">Non disponibile</label>
    </p>
    <button type="submit"  onclick="changeAvailability()">UPDATE</button>
</div>


<%--insert new plate--%>
<div>
    <h2>MODIFICA MENU'</h2>
    <label for="restaurant">ristorante:</label>
    <select id="restaurant" name="ristorante">
        <option value="Vegetale" selected>Vegetale</option>
        <option value="DeCarlo">DeCarlo</option>
        <option value="Paradiso">Paradiso</option>
        <option value="Sushino">Sushino</option>
        <option value="Tramonto">Tramonto</option>
        <option value="YinDyan">YinDyan</option>
    </select>

    <br>
    <form id="newDish" action="">
        <input type="text" id="nomePiatto" name="nome piatto" placeholder="Nome" required>
        <input type="number" id="prezzoPiatto" name = "prezzo" placeholder="Prezzo" required>
        <input class="submit" type="submit">
    </form>
    <script>
        $(function (){
            $('#newDish').on('submit',function (e){
                e.preventDefault();
                addRow()
            })
        })</script>

    <table id="new-menu">

    </table>

    <button type="submit"  onclick="updateMenu()">UPDATE</button>
</div>

</body>
</html>
