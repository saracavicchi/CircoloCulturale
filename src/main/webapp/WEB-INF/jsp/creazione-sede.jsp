<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 13/07/2024
  Time: 10:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Crea Nuova Sede</title>
    <script>
        var errorDisplayed = false;

        document.addEventListener('DOMContentLoaded', function() {

            initCreaSedeForm();
            addGiorniChiusura();


        });

        function addGiorniChiusura() {
            var maxChiusure = 10;
            var chiusureCount = 0; // Start with 0 closure dates

            document.getElementById('aggiungiChiusura').addEventListener('click', function () {
                var allFilled = true;
                var uniqueDate = true;
                var today = new Date().toISOString().split('T')[0]; // Get today's date in yyyy-MM-dd format

                var enteredDates = [];
                for (var i = 1; i <= chiusureCount; i++) {
                    var chiusuraValue = document.getElementById('chiusura' + i).value;
                    if (chiusuraValue === '' || chiusuraValue <= today) {
                        allFilled = false;
                        break;
                    }
                    if (enteredDates.includes(chiusuraValue)) {
                        uniqueDate = false;
                        break;
                    }
                    enteredDates.push(chiusuraValue);
                }

                if (!allFilled && chiusureCount > 0) { // Check if all fields are filled only if there's at least one field
                    alert('Per favore, compila tutti i campi dei giorni di chiusura con date future prima di aggiungere un nuovo giorno.');
                    return;
                }

                if (!uniqueDate) {
                    alert('La data inserita è già stata selezionata. Per favore, inserisci una data diversa.');
                    return;
                }

                if (chiusureCount < maxChiusure && (allFilled || chiusureCount === 0) && uniqueDate) { // Allow adding if no fields are present yet
                    chiusureCount++;
                    var newInput = document.createElement('input');
                    newInput.type = 'date';
                    newInput.name = 'chiusura';
                    newInput.id = 'chiusura' + chiusureCount;

                    var newLabel = document.createElement('label');
                    newLabel.htmlFor = 'chiusura' + chiusureCount;
                    newLabel.textContent = 'Giorno di chiusura:';

                    var container = document.getElementById('chiusureContainer');
                    container.appendChild(newLabel);
                    container.appendChild(newInput);
                } else if (chiusureCount >= maxChiusure) {
                    alert('Massimo numero di giorni di chiusura raggiunto.');
                }
            });
        }


        function initCreaSedeForm() {
            var creaCorsoForm = document.getElementById('creaSedeForm');
            if (creaCorsoForm) {
                creaCorsoForm.addEventListener('submit', submitForm);
                var inputs = creaCorsoForm.getElementsByTagName('input');
                addFocusListenersToInputs(inputs, 'creaSedeForm');
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
            var charSpaceDashRegex = /^[A-Za-z\s\-]+$/;
            var maxLengthNome = 30;
            var maxLengthTotal = 80;

            var nome = document.getElementById('nome').value;
            var stato = document.getElementById('stato').value;
            var provincia = document.getElementById('provincia').value;
            var citta = document.getElementById('citta').value;
            var via = document.getElementById('via').value;
            var numeroCivico = document.getElementById('numeroCivico').value;

            // Validazione nome
            if (!nome.match(charSpaceDashRegex) || nome.length > maxLengthNome) {
                erroredField = "nome";
                errorMsg = "Nome deve contenere solo caratteri, spazi, trattini e deve essere di massimo 30 caratteri.";
                return false;
            }

            // Validazione stato, provincia, citta, via per caratteri, spazi e trattini
            if (!stato.match(charSpaceDashRegex) || !provincia.match(charSpaceDashRegex) ||
                !citta.match(charSpaceDashRegex) || !via.match(charSpaceDashRegex)) {
                erroredField = "stato/provincia/citta/via";
                errorMsg = "Stato, Provincia, Città, Via devono contenere solo caratteri, spazi, trattini.";
                return false;
            }

            // Validazione lunghezza totale di stato, provincia, citta, via, numero civico
            var totalLength = stato.length + provincia.length + citta.length + via.length + numeroCivico.length;
            if (totalLength > maxLengthTotal) {
                erroredField = "stato/provincia/citta/via/numeroCivico";
                errorMsg = "La somma delle lunghezze di Stato, Provincia, Città, Via, Numero Civico non deve superare 80 caratteri.";
                return false;
            }

            // Validazione giorni di chiusura successivi alla data attuale e entro un anno
            var today = new Date();
            var todayFormatted = today.toISOString().split('T')[0]; // Ottieni la data attuale in formato yyyy-MM-dd
            var nextYear = new Date(today.setFullYear(today.getFullYear() + 1)).toISOString().split('T')[0]; // Calcola la data di un anno dopo

            var chiusureInputs = document.querySelectorAll('input[name="chiusura"]');
            for (var i = 0; i < chiusureInputs.length; i++) {
                if (chiusureInputs[i].value <= todayFormatted || chiusureInputs[i].value > nextYear) {
                    erroredField = "chiusura" + (i + 1);
                    errorMsg = "I giorni di chiusura devono essere successivi alla data attuale e entro un anno.";
                    return false;
                }
            }

            if(!validateOrariAperturaChiusura()) {
                return false;
            }

            return true;
        }

        function validateOrariAperturaChiusura() {
            for(let i = 1; i <= 7; i++) {
                let orarioApertura = document.getElementById('apertura' + i).value;
                let orarioChiusura = document.getElementById('chiusura' + i).value;

                if(orarioApertura >= orarioChiusura) {
                    erroredField = "apertura" + i + "/chiusura" + i;
                    errorMsg = "L'orario di apertura deve essere antecedente all'orario di chiusura per ogni giorno.";
                    return false;
                }
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
                var h2Element = document.getElementsByTagName('h2')[0];
                displayErrorMessages(h2Element);
            } else {  // Se la validazione ha esito positivo, invia il form
                // Usa l'ID del form per inviarlo direttamente
                document.getElementById('creaSedeForm').submit();
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

<h2>Crea Nuova Sede</h2>
<% String addressAlreadyPresent;
    if ((addressAlreadyPresent = request.getParameter("addressAlreadyPresent")) != null && addressAlreadyPresent.equals("true")) {
%>
<p>Errore, esiste già una sede all'indirizzo digitato</p>
<%
    }
%>
<% String nameAlreadyPresent;
    if ((nameAlreadyPresent = request.getParameter("nameAlreadyPresent")) != null && nameAlreadyPresent.equals("true")) {
%>
<p>Errore, esiste già una sede con lo stesso nome</p>
<%
    }
%>
<% String fail;
    if ((fail = request.getParameter("fail")) != null && fail.equals("true")) {
%>
<p id="fail">Errore durante la creazione della sede, verificare le informazioni e riprovare</p>
<script>
    var errorPresentElement = document.getElementById("fail");
    errorPresentElement.scrollIntoView({behavior: "smooth"});
</script>
<%} %>
<form id="creaSedeForm" action="/sede/crea" method="post">
    <label>Nome:</label>
    <input type="text" id="nome" name="nome" required placeholder="Nome sede"><br>

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

    <label for="areaRistoro">Area Ristoro:</label>
    <input type="checkbox" id="areaRistoro" name="areaRistoro">

    <!-- Aggiungi i campi per gli orari di apertura e chiusura per ogni giorno della settimana -->
    <div id="orariAperturaChiusura">
        <%
            String[] giorniSettimana = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica"};
            for(int i = 0; i < 7; i++) {
        %>
        <div>
            <label>Orario di apertura (<%=giorniSettimana[i]%>):</label>
            <input type="time" id="apertura<%=i+1%>" name="orarioApertura" required>

            <label for="chiusura<%=i+1%>">Orario di chiusura (<%=giorniSettimana[i]%>):</label>
            <input type="time" id="chiusura<%=i+1%>" name="orarioChiusura" required>
        </div>
        <% } %>
    </div>

    <div id="chiusureContainer">
        <!-- <label for="chiusura1">Giorno di chiusura:</label>
        <input type="date" id="chiusura1" name="chiusura"> -->
    </div>
    <button type="button" id="aggiungiChiusura">Aggiungi un giorno di chiusura</button>


    <button type="submit">Crea Sede</button>
</form>


</body>
</html>
