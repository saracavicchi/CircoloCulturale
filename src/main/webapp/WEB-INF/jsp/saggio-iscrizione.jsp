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
            let socioIdField = document.ticketSaggioForm["socio-id"];
            socioIdField.disabled = false;

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
            let socioIdField = document.ticketSaggioForm["socio-id"];
            socioIdField.disabled = true;
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

        var errorMsg = "";
        var erroredField = "";

        function validateForm() {
            var form = document.ticketSaggioForm;
            var cf = form.cf.value;
            var name = form.name.value;
            var surname = form.surname.value;
            var dob = form.dob.value;
            var birthplace = form.birthplace.value;
            var state = form.state.value;
            var province = form.province.value;
            var city = form.city.value;
            var street = form.street.value;
            var houseNumber = form.houseNumber.value;
            var quantity = form.quantity.value;

            // Controlla se il nome, cognome, luogo di nascita, stato, provincia, città, via contengono solo caratteri e non numeri
            var regex = /^[A-Za-z\s]+$/;
            if (!regex.test(name) || !regex.test(surname) || !regex.test(birthplace) || !regex.test(state) || !regex.test(province) || !regex.test(city) || !regex.test(street)) {
                errorMsg = "I campi nome, cognome, luogo di nascita, stato, provincia, città, via devono contenere solo caratteri e non numeri.";
                erroredField = "name, surname, birthplace, state, province, city, street";
                return false;
            }

            // Controlla se la data di nascita è una data o del giorno corrente o antecedente
            var today = new Date();
            today.setHours(0, 0, 0, 0);
            var inputDate = new Date(dob);
            if (inputDate > today) {
                errorMsg = "La data di nascita deve essere odierna o antecedente.";
                erroredField = "dob";
                return false;
            }

            // Controlla se la somma dei caratteri di stato, provincia, città, via e numero civico non supera gli 80 caratteri
            if ((state.length + province.length + city.length + street.length + houseNumber.length) > 80) {
                errorMsg = "La somma dei caratteri di stato, provincia, città, via e numero civico non deve superare gli 80 caratteri.";
                erroredField = "state, province, city, street, houseNumber";
                return false;
            }

            // Controlla se il codice fiscale è composto sia da numeri che da lettere
            var cfRegex = /^[0-9a-zA-Z]+$/;
            if (!cfRegex.test(cf)) {
                errorMsg = "Il codice fiscale deve essere composto sia da numeri che da lettere.";
                erroredField = "cf";
                return false;
            }

            // Controlla se la quantità di posti è un numero inferiore alla disponibilita` massima
            if (quantity > ${availableTickets}) {
                errorMsg = "La quantità di posti non può superare la disponibilità massima.";
                erroredField = "quantity";
                return false;
            }

            // Se tutti i controlli passano, restituisce true per permettere l'invio del form
            return true;
        }

        function submitForm(event) {
            // Impedisci l'invio del form
            event.preventDefault();

            // Chiama la funzione validateForm
            var validation = validateForm();

            // Se la validazione ha esito positivo, invia il form
            if (validation) {
                var inputFields = event.target.getElementsByTagName('input');
                for (let i = 0; i < inputFields.length; i++) {
                    inputFields[i].disabled = false;
                }
                event.target.submit();
            } else {
                // Ottieni l'elemento h1
                var h1Element = document.getElementsByTagName('h1')[0];

                // Controlla se il messaggio di errore esiste già
                var errorMessageElement = document.getElementById('error-message');
                var specificErrorElement = document.getElementById('specific-error');

                // Se il messaggio di errore non esiste, crealo
                if (!errorMessageElement) {
                    errorMessageElement = document.createElement('h2');
                    errorMessageElement.id = 'error-message';
                    errorMessageElement.textContent = "Errore durante l'inserimento, si prega di correggere le informazioni errate.";
                    h1Element.appendChild(errorMessageElement);
                }

                // Se il messaggio di errore specifico non esiste, crealo
                if (!specificErrorElement) {
                    specificErrorElement = document.createElement('h2');
                    specificErrorElement.id = 'specific-error';
                    specificErrorElement.textContent = errorMsg;
                    h1Element.appendChild(specificErrorElement);
                }

                // Colora il bordo del campo o dei campi che hanno dato errore
                var fields = erroredField.split(', ');
                for (var i = 0; i < fields.length; i++) {
                    var fieldElement = document.getElementById(fields[i]);
                    fieldElement.style.border = '1px solid red';
                }

                // Fai scorrere la pagina fino all'elemento h1
                h1Element.scrollIntoView({behavior: "smooth"});
            }
        }

        function removeError(event) {
            // Rimuovi il messaggio di errore
            var errorMessageElement = document.getElementById('error-message');
            if (errorMessageElement) {
                errorMessageElement.remove();
            }

            // Rimuovi il messaggio di errore specifico
            var specificErrorElement = document.getElementById('specific-error');
            if (specificErrorElement) {
                specificErrorElement.remove();
            }

            // Ottieni tutti gli elementi input del form
            var inputs = document.getElementById('ticketSaggioForm').getElementsByTagName('input');

            // Itera su ogni elemento input
            for (var i = 0; i < inputs.length; i++) {
                // Rimuovi il bordo rosso dal campo
                inputs[i].style.border = '';
            }
        }

        window.addEventListener("load", function () {
        // Aggiungi un listener per l'evento 'submit' al form
        document.getElementById('ticketSaggioForm').addEventListener('submit', submitForm);
        // Ottieni tutti gli elementi input del form
        var inputs = document.getElementById('ticketSaggioForm').getElementsByTagName('input');

        // Aggiungi un listener per l'evento 'input' a ogni elemento input
        for (var i = 0; i < inputs.length; i++) {
            inputs[i].addEventListener('focus', removeError);
        }});
    </script>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content">
        <main class="fullsize">
            <section class="title">
                <h1>Modulo di iscrizione al saggio</h1>
            </section>
            <section class="content">
                <form id="ticketSaggioForm" name="ticketSaggioForm" action="/saggio/iscrizione" method="POST">
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
