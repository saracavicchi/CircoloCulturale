<%--
  Created by IntelliJ IDEA.
  User: lucadomeneghetti
  Date: 02/07/2024
  Time: 20:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>CircoloCulturale</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css">
    <script>
        <% if (request.getAttribute("socio") != null) { %>
        function autocompileSocio () {
            let nameField = document.getElementById("name");
            nameField.value = "${socio.utente.nome}";
            nameField.disabled = true;
            let surnameField = document.getElementById("surname");
            surnameField.value = "${socio.utente.cognome}";
            surnameField.disabled = true;
            let cfField = document.getElementById("cf");
            cfField.value = "${socio.utente.cf}";
            cfField.disabled = true;
            let dobField = document.getElementById("dob");
            dobField.value = "${socio.utente.dataNascita}";
            dobField.disabled = true;
            let birthplaceField = document.getElementById("birthplace");
            birthplaceField.value = "${socio.utente.luogoNascita}";
            birthplaceField.disabled = true;
            let address = "${socio.utente.indirizzo}";
            let addressSplitted = address.split(", ");
            let stateField = document.getElementById("state");
            stateField.value = addressSplitted[0];
            stateField.disabled = true;
            let provinceField = document.getElementById("province");
            provinceField.value = addressSplitted[1];
            provinceField.disabled = true;
            let cityField = document.getElementById("city");
            cityField.value = addressSplitted[2];
            cityField.disabled = true;
            let streetField = document.getElementById("street");
            streetField.value = addressSplitted[3];
            streetField.disabled = true;
            let houseNumberField = document.getElementById("houseNumber");
            houseNumberField.value = addressSplitted[4];
            houseNumberField.disabled = true;
        }

        function cleanUnlockFields () {
            let fields = ["name", "surname", "cf", "dob", "birthplace", "state", "province", "city", "street", "houseNumber"];
            fields.forEach(field => {
                let fieldElement = document.getElementById(field);
                fieldElement.value = "";
                fieldElement.disabled = false;
            });
        }

        function radioChange () {
            let selfRadio = document.getElementById("self");
            let otherRadio = document.getElementById("other");
            selfRadio.addEventListener("change", () => {
                if (selfRadio.checked) {
                    autocompileSocio();
                }
            });
            otherRadio.addEventListener("change", () => {
                if (otherRadio.checked) {
                    cleanUnlockFields();
                }
            });
        }

        window.addEventListener("load", radioChange)
        window.addEventListener("load", function () {
            if (document.getElementById("self").checked) {
                autocompileSocio();
            }
        })
        <% } %>
    </script>
</head>
<body>
    <%@include file="/static/include/top-bar.jsp"%>
    <div id="main-content">
        <main class="fullsize">
            <section class="title">
                <h1>Modulo di iscrizione al saggio</h1>
            </section>
            <section class="content">
                <form action="/saggio/iscrizione" method="POST">
                    <h1>${saggio.nome}</h1>
                    <h2>${saggio.descrizione}</h2>
                    <p>${saggio.data} dalle ${saggio.orarioInizio} alle ${saggio.orarioFine}</p>
                    <% if (request.getAttribute("socio") != null) { %>
                    <fieldset>
                        <legend > Intestatario biglietto:</legend >
                        <input type="radio" id="self" name="ticketUser" value="self" checked>
                        <label for="self">Personale</label>
                        <input type="radio" id="other" name="ticketUser" value="other">
                        <label for="other">Altro utente</label>
                    </fieldset>
                    <input type="hidden" name="socio-id" value="${socio.id}">
                    <% } %>
                    <label for="name">Nome:</label>
                    <input type="text" id="name" name="name" maxlength="20" required>

                    <label for="surname">Cognome:</label>
                    <input type="text" id="surname" name="surname" maxlength="20" required>

                    <label for="cf">Codice fiscale:</label>
                    <input type="text" id="cf" name="cf" maxlength="16" minlength="16" required>

                    <label for="dob">Data di nascita:</label>
                    <input type="date" id="dob" name="dob" required>

                    <label for="birthplace">Luogo di nascita (città):</label>
                    <input type="text" id="birthplace" name="birthplace" maxlength="20" required>

                    <label for="state">Stato:</label>
                    <input type="text" id="state" name="state" required>

                    <label for="province">Provincia:</label>
                    <input type="text" id="province" name="province" required>

                    <label for="city">Città:</label>
                    <input type="text" id="city" name="city" required>

                    <label for="street">Via:</label>
                    <input type="text" id="street" name="street" required>

                    <label for="houseNumber">Numero Civico:</label>
                    <input type="text" id="houseNumber" name="houseNumber" required>

                    <label for="quantity">Quantità di posti:</label>
                    <input id="quantity" name="quantity" type="number" min="1" max="${availableTickets}" required>

                    <input type="hidden" name="saggio-id" value="${saggio.id}">

                    <input type="submit" value="Iscriviti al saggio">
                </form>
            </section>
        </main>
    </div>
</body>
</html>
