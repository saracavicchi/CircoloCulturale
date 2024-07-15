<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 13/07/2024
  Time: 10:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Crea Nuova Sede</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css">
    <script>
        var errorDisplayed = false;

        document.addEventListener('DOMContentLoaded', function() {

            initCreaSedeForm();
            addGiorniChiusura();
            gestisciStipendioSegretario();


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
            var charSpaceDashRegex =/^(?=.*[A-Za-z])[A-Za-z\s\'\-]+$/;
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
                var h1Element = document.getElementsByTagName('h1')[0];
                displayErrorMessages(h1Element);
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

        /*function gestisciStipendioSegretario() {
            var selectSegretari = document.getElementsByName('segretari')[0];
            var stipendiContainer = document.getElementById('StipendiContainer');
            stipendiContainer.innerHTML = ''; // Clears the current list

            var selectedSegretario = selectSegretari.value;
            if (selectedSegretario) { // Check if a secretary is selected
                var stipendioDiv = document.createElement('div');
                var stipendioLabel = document.createElement('label');
                stipendioLabel.textContent = 'Stipendio per il segretario selezionato:';

                var stipendioInput = document.createElement('input');
                stipendioInput.setAttribute('type', 'number');
                stipendioInput.setAttribute('name', 'stipendioSegretario');
                stipendioInput.setAttribute('placeholder', 'Inserisci lo stipendio');
                stipendioInput.setAttribute('required', '');
                stipendioInput.setAttribute('min', '10000');
                stipendioInput.setAttribute('max', '150000');

                // Validate the salary input on change
                stipendioInput.addEventListener('change', function() {
                    var value = parseInt(this.value, 10);
                    if (isNaN(value) || value < 10000 || value > 150000) {
                        alert('Lo stipendio deve essere un numero intero tra 10.000 e 150.000 euro.');
                        this.value = ''; // Clear the input if validation fails
                    }
                });

                stipendioDiv.appendChild(stipendioLabel);
                stipendioDiv.appendChild(stipendioInput);
                stipendiContainer.appendChild(stipendioDiv);
            }
        }*/

        function gestisciStipendioSegretario() {
            var selectSegretari = document.getElementsByName('segretari')[0];
            var stipendiContainer = document.getElementById('AdminContainer');
            stipendiContainer.innerHTML = ''; // Clears the current list

            var selectedSegretario = selectSegretari.value;
            if (selectedSegretario) { // Check if a secretary is selected
                // Create a div for the admin checkbox
                var adminDiv = document.createElement('div');
                var adminLabel = document.createElement('label');
                adminLabel.textContent = 'Admin?';

                var adminCheckbox = document.createElement('input');
                adminCheckbox.setAttribute('type', 'checkbox');
                adminCheckbox.setAttribute('name', 'adminSegretario');
                adminCheckbox.setAttribute('id', 'adminSegretario');

                adminDiv.appendChild(adminLabel);
                adminDiv.appendChild(adminCheckbox);
                stipendiContainer.appendChild(adminDiv);
            }
        }


    </script>
</head>
<body>
<%@ include file="/static/include/header.jsp" %>
<div id="main-content">
    <main class="midleft">
        <section class="title">
        <h1>Crea Nuova Sede</h1>
        </section>
    <section class="content">
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
    <fieldset>
        <legend>Informazioni Generali</legend>
        <label>Nome:</label>
        <input type="text" id="nome" name="nome" required placeholder="Nome sede">
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
        <legend>Servizi</legend>
        <label for="areaRistoro">Area Ristoro:</label>
        <input type="checkbox" id="areaRistoro" name="areaRistoro">
    </fieldset>

    <fieldset>
    <legend>Orari di Apertura e Chiusura</legend>
    <div id="orariAperturaChiusura">
        <% String[] giorniSettimana = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica"};
            for(int i = 0; i < 7; i++) { %>
        <div>
            <label>Orario di apertura (<%=giorniSettimana[i]%>):</label>
            <input type="time" id="apertura<%=i+1%>" name="orarioApertura" required>
            <label for="chiusura<%=i+1%>">Orario di chiusura (<%=giorniSettimana[i]%>):</label>
            <input type="time" id="chiusura<%=i+1%>" name="orarioChiusura" required>
        </div>
        <% } %>
    </div>
    </fieldset>

    <fieldset>
        <legend>Giorni di Chiusura</legend>
        <div id="chiusureContainer"></div>
        <button type="button" id="aggiungiChiusura">Aggiungi un giorno di chiusura</button>
    </fieldset>
    <fieldset>
        <legend>Segretario e Amministrazione</legend>
        Segretario: <select name="segretari" onchange="gestisciStipendioSegretario()">
        <c:forEach items="${sociInfo}" var="socioS" varStatus="status">
            <option value="${socioS[3]}" ${status.index == 0 ? 'selected' : ''}>${socioS[1]} ${socioS[2]} (${socioS[0]})</option>
        </c:forEach>
        </select>
        <div id="AdminContainer"></div>
    </fieldset>
    <button type="submit">Crea Sede</button>
</form>


</section>
</main>
    <%@include file="/static/include/aside.jsp"%>
</div>


</body>
</html>
