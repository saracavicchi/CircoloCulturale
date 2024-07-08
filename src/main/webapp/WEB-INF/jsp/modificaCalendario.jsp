<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 07/07/2024
  Time: 17:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Modifica Orari Corso</title>
    <script>
        var errorDisplayed = false;
        var errorMsg = "";
        document.addEventListener('DOMContentLoaded', function() {

            initCreaCorsoForm();


        });
        function initCreaCorsoForm() {
            var creaCorsoForm = document.getElementById('modificaCalendarioForm');
            if (creaCorsoForm) {
                creaCorsoForm.addEventListener('submit', submitForm);
                var inputs = creaCorsoForm.getElementsByTagName('input');
                addFocusListenersToInputs(inputs, 'modificaCalendarioForm');
            }
        }
        function addFocusListenersToInputs(inputs, formName) {
            for (var i = 0; i < inputs.length; i++) {
                inputs[i].addEventListener('focus', function() {
                    removeError(formName);
                });
            }
        }

        function toggleTimeInputs(checkbox, giorno) {
            const displayValue = checkbox.checked ? 'block' : 'none';
            document.getElementById('orario' + giorno).style.display = displayValue;
        }

        function validateDayAndTimeSelection() {
            var giorniSettimana = document.getElementsByName('giorni');
            var isAnyDaySelected = false;
            var allTimesAreValid = true;
            var orariInizio = document.getElementsByName('orariInizio');
            var orariFine = document.getElementsByName('orariFine');

            for (var i = 0; i < giorniSettimana.length; i++) {
                if (giorniSettimana[i].checked) {
                    isAnyDaySelected = true;
                    // Assicurati che ci sia un orario di inizio e fine per ogni giorno selezionato
                    if (orariInizio.length <= i || orariFine.length <= i) {
                        allTimesAreValid = false;
                        break;
                    }

                    var orarioInizio = orariInizio[i].value;
                    var orarioFine = orariFine[i].value;

                    // Converti orarioInizio e orarioFine in oggetti Date per il confronto
                    var inizioDate = new Date("1970-01-01T" + orarioInizio + "Z");
                    var fineDate = new Date("1970-01-01T" + orarioFine + "Z");

                    if (!orarioInizio || !orarioFine || fineDate <= inizioDate) {
                        allTimesAreValid = false;
                        break;
                    }
                }
            }

            if (!isAnyDaySelected || !allTimesAreValid) {
                errorMsg = "Selezionare almeno un giorno e specificare gli orari di inizio e fine per ogni giorno selezionato. Assicurarsi che l'orario di fine sia dopo l'orario di inizio.";
                return false;
            }

            return true;
        }

        function submitForm(event) {
            // Impedisci l'invio del form
            event.preventDefault();

            // Chiama la funzione validateForm
            var validation = validateDayAndTimeSelection();

            if (!validation) {
                // Ottieni l'elemento h1

                var h2Element = document.getElementsByTagName('h2')[0];
                displayErrorMessages(h2Element);
            } else {
                // Ottiene tutti i checkbox
                var checkboxes = document.querySelectorAll('input[type="checkbox"][name="giorni"]');

                // Itera su tutti i checkbox e rimuove l'attributo 'name' da quelli non selezionati
                checkboxes.forEach(function(checkbox) {
                    if (!checkbox.checked) {
                        checkbox.removeAttribute('name');
                    }
                });
                // Se la validazione ha esito positivo, invia il form
                // Usa l'ID del form per inviarlo direttamente
                document.getElementById('modificaCalendarioForm').submit();
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
        }

        function removeError(formName) {
            if(!errorDisplayed){
                return;
            }
            errorDisplayed=false;

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

        }
        function redirectToEditDocentiPage() {
            var courseId = '${corso.id}';
            window.location.href = '/modificaDocenti?idCorso=' + courseId;
        }

        function redirectToEditCorsoPage() {
            var courseId = '${corso.id}';
            window.location.href = '/editCorso?idCorso=' + courseId;
        }
    </script>
</head>
<body>
<h2>Modifica Calendario e Sala Corso</h2>
<form id="modificaCalendarioForm" action="modificaCalendario" method="post">
    <input type="hidden" name="idCorso" value="${corso.id}"/>

    <label>Sala:</label>
    <select name="idSala" required>
        <c:forEach items="${sale}" var="sala">
            <c:choose>
                <c:when test="${sala.id.toString() == corso.idSala.id.toString()}">
                    <option value="${sala.id}" selected="selected">${sala.numeroSala}</option>
                </c:when>
                <c:otherwise>
                    <option value="${sala.id}">${sala.numeroSala}</option>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </select>

    <c:forEach var="dayIndex" begin="1" end="5">
        <c:set var="giornoIt" value="${dayIndex == 1 ? 'Lunedì' : dayIndex == 2 ? 'Martedì' : dayIndex == 3 ? 'Mercoledì' : dayIndex == 4 ? 'Giovedì' : 'Venerdì'}"/>
        <c:set var="giorno" value="${dayIndex == 1 ? 'monday' : dayIndex == 2 ? 'tuesday' : dayIndex == 3 ? 'wednesday' : dayIndex == 4 ? 'thursday' : 'friday'}"/>
        <c:set var="found" value="false"/>
        <c:forEach items="${calendarioCorso}" var="calendario" varStatus="status">
            <c:if test="${giorno == calendario.giornoSettimana and not found}">
                <label for="giorno${giorno}">${giornoIt}: </label>
                <input type="checkbox" id="giorno${giorno}" name="giorni" value="${dayIndex}" onchange="toggleTimeInputs(this,'${giorno}');" checked>
                <div id="orario${giorno}" style="display:block;">
                    Orario Inizio: <input type="time" name="orariInizio" value="${calendario.orarioInizio}"><br>
                    Orario Fine: <input type="time" name="orariFine" value="${calendario.orarioFine}"><br>
                </div>
                <br>
                <c:set var="found" value="true"/>
            </c:if>
        </c:forEach>
        <c:if test="${not found}">
            <label for="giorno${giorno}">${giornoIt}: </label>
            <input type="checkbox" id="giorno${giorno}" name="giorni" value="${dayIndex}" onchange="toggleTimeInputs(this,'${giorno}');">
            <div id="orario${giorno}" style="display:none;">
                Orario Inizio: <input type="time" name="orariInizio"><br>
                Orario Fine: <input type="time" name="orariFine"><br>
            </div>
            <br>
        </c:if>
    </c:forEach>
    <button type="submit">Salva Modifiche</button>
</form>
<button type="button" onclick="redirectToEditDocentiPage()">Modifica Docenti</button>
<button type="button" onclick="redirectToEditCorsoPage()">Torna alla pagina modifica corso</button>
</body>
</html>