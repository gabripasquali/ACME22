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
    <script>
        //we need the id of the res
        function changeAvailability(){
            let availability = document.getElementById("availability").value;
            let requestBody = {
                "name" : document.getElementById("restaurant").value.toString(),
                "availability" : availability.toString()
            };

            let availabilityUrl ="changeAvailability";
            let xhr = new XMLHttpRequest();
            xhr.open("PUT", availabilityUrl, true);
            xhr.send(JSON.stringify(requestBody));

            xhr.onreadystatechange = function (){
                console.log("RESPONSE ON ABORT REQUEST");
                let resp = xhr.responseText
                console.log(resp);
            }
        }

        function updateMenu(){
            let updateUrl ="http://localhost:8080/acme-api/changeMenu";
            //read new plate
            let requestBody = {
                "name" : document.getElementById("restaurant").value.toString()
                //menu update
            };

            let xhr = new XMLHttpRequest();
            xhr.open("PUT", updateUrl, true);
            xhr.send(JSON.stringify(requestBody));

            xhr.onreadystatechange = function (){
                console.log("RESPONSE ON UPDATE MENU REQUEST");
                let resp = xhr.responseText
                console.log(resp);
            }
        }
    </script>
</head>
<body>
<h1> PAGINA RISTORANTI</h1>
<label for="restaurant">Nome ristorante:</label>
<input type="text" id="restaurant" value="ristorante">

<p>Disponibilità odierna:</p>
<select id="availability" name="availability">
    <option value="true" selected>SI</option>
    <option value="false">NO</option>
</select>
<button type="submit"  onclick="changeAvailability()">test</button>
<%--insert new plate--%>

<button type="submit"  onclick="updateMenu()">test</button>
</body>
</html>
