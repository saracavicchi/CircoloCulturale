<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 13/07/2024
  Time: 11:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Creazione Sala</title>
    <script>
        var errorDisplayed = false;

        document.addEventListener('DOMContentLoaded', function() {
            initCreaSalaForm();
        });

        function initCreaSaggioForm() {
            var creaSalaorm = document.getElementById('creaSalaForm');
            if (creaSalaForm) {
                creaSalaForm.addEventListener('submit', submitForm);
                var inputs = creaSalaForm.getElementsByTagName('input');
                addFocusListenersToInputs(inputs, 'creaSalaForm');
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
            var numeroSala = document.getElementById('numero').value;
            var capienza = document.getElementById('capienza').value;

            // Controlla che il numero della sala sia presente e contenga solo numeri
            if (!numeroSala || !/^\d+$/.test(numeroSala)) {
                alert("Il numero della sala deve essere presente e può contenere solo numeri.");
                return false;
            }

            // Controlla che la capienza sia presente e sia un intero positivo
            if (!capienza || !/^\d+$/.test(capienza) || parseInt(capienza, 10) <= 0) {
                alert("La capienza deve essere un numero intero positivo.");
                return false;
            }

            return true; // La validazione è passata
        }

        function submitForm(event) {
            // Impedisci l'invio del form
            event.preventDefault();

            // Chiama la funzione validateForm
            var validation = validateForm();

            if (!validation) {
                // Ottieni l'elemento h1
                var h2Element = document.getElementsByTagName('h2')[0];
                displayErrorMessages(h2Element);
            } else {  // Se la validazione ha esito positivo, invia il form
                // Usa l'ID del form per inviarlo direttamente
                document.getElementById('creaSalaForm').submit();
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
<h2>Crea una nuova Sala</h2>
<% String alreadyPresent;
    if ((alreadyPresent = request.getParameter("alreadyPresent")) != null && alreadyPresent.equals("true")) {
%>
<p>Errore, esiste già una sala con lo stesso numero nella sede con id <%= request.getParameter("idSede") %></p>
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
<%} %>
<form id="creaSalaForm" action="/sala/crea" method="post">
    <input type="hidden" name="idSede" value="<%= request.getParameter("idSede") %>">

    <label for="numero">Numero Sala:</label>
    <input type="text" id="numero" name="numero" required pattern="\d+" title="Solo numeri sono permessi."><br>

    <label for="capienza">Capienza:</label>
    <input type="number" id="capienza" name="capienza" required><br>

    <label for="descrizione">Descrizione (Opzionale):</label>
    <textarea id="descrizione" name="descrizione"></textarea><br>

    <label for="prenotabile">Prenotabile:</label>
    <input type="checkbox" id="prenotabile" name="prenotabile">

    <button type="submit">Crea Sala</button>
</form>
</body>
</html>