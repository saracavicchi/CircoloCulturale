<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 09/07/2024
  Time: 22:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Creazione Saggio</title>
    <script>
        var errorDisplayed = false;

        document.addEventListener('DOMContentLoaded', function() {

            initCreaSaggioForm();


        });
        function initCreaSaggioForm() {
            var creaCorsoForm = document.getElementById('creaSaggioForm');
            if (creaCorsoForm) {
                creaCorsoForm.addEventListener('submit', submitForm);
                var inputs = creaCorsoForm.getElementsByTagName('input');
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

            var charSpaceDashRegex = /^[A-Za-z\s\-]+$/;

            if (!nome.match(charSpaceDashRegex) || !nome || nome.length > 30 || nome == "") {
                errorMsg = "Nome deve contenere solo lettere, spazi o trattini e deve essere di massimo 30 caratteri";
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
            if (descrizione && !descrizione.match(charSpaceDashRegex)) {
                erroredField = "descrizione";
                errorMsg += "Descrizione deve contenere solo lettere, spazi o trattini";
                return false;
            }

            if (orarioInizio && !orarioInizio.match(/^\d{2}:\d{2}$/)) {
                errorMsg = "Orario inizio non valido.\n";
            }

            if (orarioFine && !orarioFine.match(/^\d{2}:\d{2}$/)) {
                errorMsg = "Orario fine non valido.\n";
            }

            if (orarioInizio && orarioFine && orarioInizio >= orarioFine) {
                errorMsg = "Orario inizio deve essere prima dell'orario fine.\n";
            }
            // Controlla se la somma dei caratteri di stato, provincia, città, via e numero civico non supera gli 80 caratteri
            if ((country.length + province.length + city.length + street.length + houseNumber.length) > 80 || !country || !province || !city || !street || !houseNumber) {
                errorMsg = "La somma dei caratteri di stato, provincia, città, via e numero civico non deve superare gli 80 caratteri.";
                erroredField = "country, province, city, street, houseNumber";
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
                // Ottieni l'elemento h1
                var h1Element = document.getElementsByTagName('h2')[0];
                displayErrorMessages(h2Element);
            } else {  // Se la validazione ha esito positivo, invia il form
                // Usa l'ID del form per inviarlo direttamente
                document.getElementById('creaSaggioForm').submit();
            }
        }
        function displayErrorMessages(Element) {

            errorDisplayed = true;
            // Controlla se il messaggio di errore esiste già
            var errorMessageElement = document.getElementById('error-message');
            var specificErrorElement = document.getElementById('specific-error');

            // Se il messaggio di errore non esiste, crealo
            if (!errorMessageElement) {
                errorMessageElement = document.createElement('p');
                errorMessageElement.id = 'error-message';
                errorMessageElement.textContent = "Errore durante l'inserimento, si prega di correggere le informazioni errate.";
                Element.appendChild(errorMessageElement);
            }

            // Se il messaggio di errore specifico non esiste, crealo
            if (!specificErrorElement) {
                specificErrorElement = document.createElement('p');
                specificErrorElement.id = 'specific-error';
                specificErrorElement.textContent = errorMsg;
                Element.appendChild(specificErrorElement);

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
</head>
<body>
<h2>Creazione Saggio</h2>
<% String fail;
    if ((fail = request.getParameter("fail")) != null && fail.equals("true")) {
%>
<p>Errore durante la creazione del saggio, verificare le informazioni e riprovare</p>
<%
    }
%>
<form id="creaSaggioForm" action="creaSaggio" method="post">
    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" required maxlength="30" pattern="[A-Za-z\s\-]+" placeholder="Nome del saggio">

    <label for="data">Data:</label>
    <input type="date" id="data" name="data" required>

    <label for="numeroPartecipanti">Numero massimo partecipanti:</label>
    <input type="number" id="numeroPartecipanti" name="numeroPartecipanti" required min="1" placeholder="Numero massimo partecipanti">

    <label for="descrizione">Descrizione:</label>
    <textarea id="descrizione" name="descrizione" placeholder="Descrizione del saggio (facoltativo)"></textarea>

    <label for="orarioInizio">Orario inizio:</label>
    <input type="time" id="orarioInizio" name="orarioInizio" placeholder="Orario di inizio (facoltativo)">

    <label for="orarioFine">Orario fine:</label>
    <input type="time" id="orarioFine" name="orarioFine" placeholder="Orario di fine (facoltativo)">

    <label for="country">Stato:</label>
    <input type="text" id="country" name="country" placeholder="Stato" required>

    <label for="province">Provincia:</label>
    <input type="text" id="province" name="province" placeholder="Provincia" required>

    <label for="city">Città:</label>
    <input type="text" id="city" name="city" placeholder="Città" required>

    <label for="street">Via:</label>
    <input type="text" id="street" name="street" placeholder="Via" required>

    <label for="houseNumber">Numero Civico:</label>
    <input type="text" id="houseNumber" name="houseNumber" placeholder="Numero civico" required>

    <input type="file" id="photo" name="photo">

    <button type="submit">Crea Saggio</button>
</form>
</body>
</html>
