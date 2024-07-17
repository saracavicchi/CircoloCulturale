<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 09/07/2024
  Time: 22:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Creazione Saggio</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css">
    <script>
        var errorDisplayed = false;

        document.addEventListener('DOMContentLoaded', function() {
            initCreaSaggioForm();
        });

        function initCreaSaggioForm() {
            var creaSaggioForm = document.getElementById('creaSaggioForm');
            if (creaSaggioForm) {
                creaSaggioForm.addEventListener('submit', submitForm);
                var inputs = creaSaggioForm.getElementsByTagName('input');
                addFocusListenersToInputs(inputs, 'creaSaggioForm');
            }
        }
        function addFocusListenersToInputs(inputs, formName) {
            for (var i = 0; i < inputs.length; i++) {
                inputs[i].addEventListener('focus', function() {
                    removeError(formName);
                });
            }
        }

        //campo/i che ha generato l'errore
        var erroredField = "";
        var errorMsg = "";

        function validateForm() {
            var nome = document.getElementById('nome').value;
            var data = document.getElementById('data').value;
            var numeroPartecipanti = document.getElementById('numeroPartecipanti').value;
            var descrizione = document.getElementById('descrizione').value;
            var orarioInizio = document.getElementById('orarioInizio').value;
            var orarioFine = document.getElementById('orarioFine').value;
            var stato = document.getElementById('stato').value;
            var provincia = document.getElementById('provincia').value;
            var citta = document.getElementById('citta').value;
            var via = document.getElementById('via').value;
            var numeroCivico = document.getElementById('numeroCivico').value;

            var charSpaceDashRegex = /^(?=.*[A-Za-z])[A-Za-z\s\'\-]+$/;
            var charDescrizioneRegex = /^(?=.*[A-Za-z])[A-Za-z\s\'\-\(\)\.\,\;\:\!\?\[\]\{\}\"\-]+$/;

            if (!nome.match(charSpaceDashRegex) || !nome || nome.length > 30 || nome == "") {
                errorMsg = "Nome contiene alcuni caratteri non validi e deve essere di massimo 30 caratteri";
                erroredField = "nome";
                return false;
            }

            var today = new Date().toISOString().split('T')[0]; // Get today's date in yyyy-MM-dd format
            if (data="" || data < today || !data) {
                errorMsg = "Data non valida";
                return false;
            }


            if (numeroPartecipanti <= 0 || isNaN(numeroPartecipanti) || !numeroPartecipanti) {
                errorMsg="Numero massimo partecipanti deve essere un numero positivo.";
                erroredField = "numeroPartecipanti";
                return false;
            }

            // Optional fields validation
            if (descrizione && !descrizione.match(charDescrizioneRegex)) {
                errorMsg = "Descrizione deve contenere solo lettere, spazi o trattini";
                return false;
            }

            if (orarioInizio && !orarioInizio.match(/^\d{2}:\d{2}$/)) {
                errorMsg = "Orario inizio non valido.\n";
            }

            if (orarioFine && !orarioFine.match(/^\d{2}:\d{2}$/)) {
                errorMsg = "Orario fine non valido.\n";
            }

            if (orarioInizio && orarioFine) {
                var orarioInizioDate = new Date("1970-01-01T" + orarioInizio + "Z");
                var orarioFineDate = new Date("1970-01-01T" + orarioFine + "Z");

                if (orarioInizioDate >= orarioFineDate) {
                    errorMsg = "Orario inizio deve essere prima dell'orario fine.";
                    return false;
                }
            }
            // Controlla se la somma dei caratteri di stato, provincia, città, via e numero civico non supera gli 80 caratteri
            if ((stato.length + provincia.length + citta.length + via.length + numeroCivico.length) > 80 || !stato || !provincia || !citta || !via || !numeroCivico) {
                errorMsg = "La somma dei caratteri di stato, provincia, città, via e numero civico non deve superare gli 80 caratteri.";
                erroredField = "stato, provincia, citta, via, numeroCivico";
                return false;
            }

            if (!stato.match(charSpaceDashRegex) || !stato || stato == "" ) {
                errorMsg = "Lo stato contiene alcuni caratteri non validi e deve essere di massimo 30 caratteri";
                erroredField = "stato";
                return false;
            }

            if (!provincia.match(charSpaceDashRegex) || !provincia || provincia == "" ) {
                errorMsg = "La provincia contiene alcuni caratteri non validi e deve essere di massimo 30 caratteri";
                erroredField = "provincia";
                return false;
            }

            if (!citta.match(charSpaceDashRegex) || !citta || citta == "" ) {
                errorMsg = "La città contiene alcuni caratteri non validi e deve essere di massimo 30 caratteri";
                erroredField = "citta";
                return false;
            }

            if (!via.match(charSpaceDashRegex) || !via || via == "" ) {
                errorMsg = "La via contiene alcuni caratteri non validi e deve essere di massimo 30 caratteri";
                erroredField = "via";
                return false;
            }
            if(!validateCourseSelection())
                return false;

            return true;
        }

        function validateCourseSelection() {
            var selectCorsi = document.getElementsByName('corsi')[0];

            if (selectCorsi.selectedOptions.length === 0) {
                errorMsg="Selezionare almeno un corso";
                return false;
            }
            return true;
        }

        function submitForm(event) {
            // Impedisci l'invio del form
            event.preventDefault();

            // Chiama la funzione validateForm
            var validation = validateForm();

            if (!validation) {
                displayErrorMessages();
            } else {  // Se la validazione ha esito positivo, invia il form
                // Usa l'ID del form per inviarlo direttamente
                document.getElementById('creaSaggioForm').submit();
            }
        }
        function displayErrorMessages(Element) {
            var formElement = document.getElementById('creaSaggioForm');
            errorDisplayed = true;
            // Controlla se il messaggio di errore esiste già
            var errorMessageElement = document.getElementById('error-message');
            var specificErrorElement = document.getElementById('specific-error');

            // Se il messaggio di errore non esiste, crealo
            if (!errorMessageElement) {
                errorMessageElement = document.createElement('p');
                errorMessageElement.id = 'error-message';
                errorMessageElement.textContent = "Errore durante l'inserimento, si prega di correggere le informazioni errate.";
                document.querySelector('.content').insertBefore(errorMessageElement, formElement);
            }

            // Se il messaggio di errore specifico non esiste, crealo
            if (!specificErrorElement) {
                specificErrorElement = document.createElement('p');
                specificErrorElement.id = 'specific-error';
                specificErrorElement.textContent = errorMsg;
                document.querySelector('.content').insertBefore(specificErrorElement, formElement);

            }

            // Colora il bordo del campo o dei campi che hanno dato errore
            if(erroredField != "") {
                var fields = erroredField.split(', ');
                for (var i = 0; i < fields.length; i++) {
                    var fieldElement = document.getElementById(fields[i]);
                    if (fieldElement) {
                        fieldElement.style.border = '1px solid red';
                    }
                }
            }
            if(errorMessageElement) {
                scrollToErrorMsg();
            }

        }
        function scrollToErrorMsg() {
            var ErrorMsgElement = document.getElementById('error-message');
            if (ErrorMsgElement) {
                ErrorMsgElement.scrollIntoView({behavior: "smooth"});
            }
        }

        function removeError(formName) {
            if(!errorDisplayed){
                return;
            }
            errorDisplayed=false;
            erroredField="";
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
            var allInputs = document.getElementById(formName).getElementsByTagName('input');
            // Filtra per ottenere solo gli input di tipo 'text' o number
            var inputs = Array.from(allInputs).filter(function(input) {
                return input.type === 'text' || input.type === 'number';
            });

            // Itera su ogni elemento input
            for (var i = 0; i < inputs.length; i++) {
                // Rimuovi il bordo rosso dal campo
                inputs[i].style.border = '';
            }
        }
    </script>
    <style>

    </style>
</head>
<body>
<%@ include file="/static/include/header.jsp" %>
<div id="main-content" class="clearfix">
    <main class="midleft">
        <section class="title">
            <h1>Creazione Saggio</h1>
        </section>
        <section class="content">

        <% String dateAlreadyPresent;
            if ((dateAlreadyPresent = request.getParameter("dateAlreadyPresent")) != null && dateAlreadyPresent.equals("true")) {
        %>
        <p>Errore, esiste già un saggio per la data selezionata</p>
        <%
            }
        %>
        <% String nameAlreadyPresent;
            if ((nameAlreadyPresent = request.getParameter("nameAlreadyPresent")) != null && nameAlreadyPresent.equals("true")) {
        %>
        <p>Errore, esiste già un saggio con lo stesso nome</p>
        <%
            }
        %>
        <% String fail;
            if ((fail = request.getParameter("fail")) != null && fail.equals("true")) {
        %>
        <p id="fail">Errore durante la creazione del saggio, verificare le informazioni e riprovare</p>
        <script>
            var errorPresentElement = document.getElementById("fail");
            errorPresentElement.scrollIntoView({behavior: "smooth"});
        </script>
        <%
        } %>
            <form id="creaSaggioForm" action="/saggio/crea" method="post" enctype="multipart/form-data">
                <fieldset>
                    <legend>Informazioni Generali</legend>
                    <label for="nome">Nome:</label>
                    <input type="text" id="nome" name="nome" required maxlength="30" placeholder="Nome del saggio">
                    <label for="data">Data:</label>
                    <input type="date" id="data" name="data" required>
                    <label for="numeroPartecipanti">Numero massimo partecipanti:</label>
                    <input type="number" id="numeroPartecipanti" name="numeroPartecipanti" required min="1" placeholder="Numero massimo partecipanti">
                    <label for="descrizione">Descrizione:</label>
                    <textarea id="descrizione" name="descrizione" placeholder="Descrizione del saggio (facoltativo)"></textarea>
                </fieldset>
                <fieldset>
                    <legend>Dettagli Evento</legend>
                    <label for="orarioInizio">Orario inizio:</label>
                    <input type="time" id="orarioInizio" name="orarioInizio">
                    <label for="orarioFine">Orario fine:</label>
                    <input type="time" id="orarioFine" name="orarioFine">
                    <label for="stato">Stato:</label>
                    <input type="text" id="stato" name="stato" placeholder="Stato" required>
                    <label for="provincia">Provincia:</label>
                    <input type="text" id="provincia" name="provincia" placeholder="Provincia" required>
                    <label for="citta">Città:</label>
                    <input type="text" id="citta" name="citta" placeholder="Città" required>
                    <label for="via">Via:</label>
                    <input type="text" id="via" name="via" placeholder="Via" required>
                    <label for="numeroCivico">Numero Civico:</label>
                    <input type="text" id="numeroCivico" name="numeroCivico" placeholder="Numero civico" required>
                </fieldset>
                <fieldset>
                    <legend>Corsi Associati</legend>
                    <label for="corsi">Seleziona Corsi:</label>
                    <select id="corsi" name="corsi" multiple>
                        <c:forEach items="${corsi}" var="corso">
                            <option value="${corso.id}">${corso.categoria}, ${corso.genere}, ${corso.livello}</option>
                        </c:forEach>
                    </select>
                </fieldset>
                <fieldset>
                    <legend>Foto</legend>
                    <label for="photo">Carica una foto per il saggio:</label>
                    <input type="file" id="photo" name="photo">
                </fieldset>
                <button type="submit">Crea Saggio</button>
            </form>
        </section>
    </main>
    <%@include file="/static/include/aside.jsp"%>
</div>
<%@include file="/static/include/footer.jsp"%>
</body>
</html>
