<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 06/07/2024
  Time: 13:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title>Modifica Corso</title>
    <script>
        var errorDisplayed = false;
        document.addEventListener('DOMContentLoaded', function () {
            setupCheckboxChangeHandlers();
            updateDocentiSelection();
            initEditCorsoForm();
        });


    function setupCheckboxChangeHandlers() {
        document.querySelectorAll('input[type="checkbox"][name="giorni"]').forEach(checkbox => {
        checkbox.addEventListener('change', handleCheckboxChange);
        });
    }

    function handleCheckboxChange() {
        const giorno = this.value;
        const orarioDivId = createOrarioDivId(giorno);
        let orarioDiv = document.getElementById(orarioDivId);

        if (this.checked) {
            if (!orarioDiv) {
                orarioDiv = createOrarioDiv(giorno, orarioDivId, this.parentNode);
            }
            orarioDiv.style.display = '';
        } else {
            if (orarioDiv) {
                orarioDiv.style.display = 'none';
            }
         }
    }

    function createOrarioDivId(giorno) {
        return 'orario' + giorno.charAt(0).toUpperCase() + giorno.slice(1);
    }

        function createOrarioDiv(giorno, orarioDivId, parentNode) {
            const orarioDiv = document.createElement('div');
            orarioDiv.id = orarioDivId;
            parentNode.appendChild(orarioDiv);

            const labelOrari = document.createElement('label');
            labelOrari.textContent = "Selezionare i nuovi orari orari:";
            orarioDiv.appendChild(labelOrari);

            const orarioInizio = document.createElement('input');
            orarioInizio.type = 'time';
            orarioInizio.name = 'orariInizio';
            orarioDiv.appendChild(document.createTextNode('Orario Inizio: '));
            orarioDiv.appendChild(orarioInizio);

            const orarioFine = document.createElement('input');
            orarioFine.type = 'time';
            orarioFine.name = 'orariFine';
            orarioDiv.appendChild(document.createElement('br'));
            orarioDiv.appendChild(document.createTextNode('Orario Fine: '));
            orarioDiv.appendChild(orarioFine);

            return orarioDiv;
        }

        function updateDocentiSelection() {
            gestisciStipendiDocenti();
        }


        function gestisciStipendiDocenti() {
            var selectDocenti = document.getElementsByName('docenti')[0];
            var docentiStipendiContainer = document.getElementById('docentiStipendiContainer');
            docentiStipendiContainer.innerHTML = ''; // Clears the current list

            Array.from(selectDocenti.selectedOptions).forEach(function(option) {
                var docenteDiv = document.createElement('div');
                var docenteLabel = document.createElement('label');
                docenteLabel.textContent = 'Stipendio per: ' + option.text;

                var docenteStipendio = document.createElement('input');
                docenteStipendio.setAttribute('type', 'number');
                docenteStipendio.setAttribute('name', 'stipendi');
                docenteStipendio.setAttribute('required', '');
                docenteStipendio.setAttribute('min', '10000');
                docenteStipendio.setAttribute('max', '100000');

                docenteDiv.appendChild(docenteLabel);
                docenteDiv.appendChild(docenteStipendio);
                docentiStipendiContainer.appendChild(docenteDiv);
            });
        }
        function initEditCorsoForm() {
            var editCorsoForm = document.getElementById('editCorsoForm');
            if (editCorsoForm) {
                editCorsoForm.addEventListener('submit', submitForm);
                var inputs = editCorsoForm.getElementsByTagName('input');
                addFocusListenersToInputs(inputs, 'editCorsoForm');
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

            var descrizione = document.getElementById('descrizione').value;
            var genere = document.getElementById('genere').value;
            var livello = document.getElementById('livello').value;
            var categoriaDanza = document.getElementById('danza').checked;
            var categoriaMusica = document.getElementById('musica').checked;


            if (!descrizione.match(charSpaceDashRegex) || descrizione==null || descrizione=="") {
                erroredField = "descrizione";
                errorMsg = "Descrizione deve contenere solo caratteri";
                return false;
            }

            if (!genere.match(charSpaceDashRegex) || genere.length > 20 || genere==null || genere=="") {
                erroredField = "genere";
                errorMsg = "Genere deve contenere solo caratteri e deve essere di massimo 20 caratteri. ";
                return false;
            }

            if (!livello.match(charSpaceDashRegex) || livello.length > 20 || livello==null || livello=="") {
                erroredField = "livello";
                errorMsg = "Livello deve contenere solo lettere, spazi o trattini e deve essere di massimo 20 caratteri. ";
                return false;
            }

            if (!categoriaDanza && !categoriaMusica) {
                errorMsg = "Selezionare una sola categoria.";
                return false;
            }
            if(!validateInstructorSelection()){
                return false;
            }
            if(!validateDayAndTimeSelection()){
                return false;
            }
            if(!validateSalaryRange()){
                return false;
            }

            return true;
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
        function validateInstructorSelection() {
            var selectDocenti = document.getElementsByName('docenti')[0];

            if (selectDocenti.selectedOptions.length === 0) {
                errorMsg="Selezionare almeno un docente";
                return false;
            }
            return true;
        }

        function submitForm(event) {
            // Impedisci l'invio del form
            event.preventDefault();

            // Chiama la funzione validateForm
            var validation = validateForm();
            console.log(validation);

            if (!validation) {
                // Ottieni l'elemento h1
                var h2Element = document.getElementsByTagName('h2')[0];
                displayErrorMessages(h2Element);
            } else {  // Se la validazione ha esito positivo, invia il form
                // Usa l'ID del form per inviarlo direttamente
                document.getElementById('editCorsoForm').submit();
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
            if(erroredField != ""){
                var fields = erroredField.split(', ');
                for (var i = 0; i < fields.length; i++) {
                    var fieldElement = document.getElementById(fields[i]);
                    if (fieldElement) {
                        fieldElement.style.border = '1px solid red';
                    }
                }
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
            // Filtra per ottenere solo gli input di tipo 'text'
            var inputs = Array.from(allInputs).filter(function(input) {
                return input.type === 'text';
            });

            // Itera su ogni elemento input
            for (var i = 0; i < inputs.length; i++) {
                // Rimuovi il bordo rosso dal campo
                inputs[i].style.border = '';
            }
        }

        function validateSalaryRange() {
            var stipendiInputs = document.querySelectorAll('#docentiStipendiContainer input[type="number"]');
            for (var i = 0; i < stipendiInputs.length; i++) {
                var stipendio = parseInt(stipendiInputs[i].value, 10);
                if (isNaN(stipendio) || stipendio < 10000 || stipendio > 100000) {
                    erroredField = stipendiInputs[i].name;
                    errorMsg = "Lo stipendio deve essere tra 10000 e 100000.";
                    return false;
                }
            }
            return true;
        }

    </script>
</head>
<body>
<h2>Modifica Corso</h2>
<form id="editCorsoForm" action="editCorso" method="post">
    <input type="hidden" name="idCorso" value="${corso.id}"/>

    <label>Descrizione:</label>
    <input type="text" id="descrizione" name="descrizione" value="${corso.descrizione}"/>

    <label>Genere:</label>
    <input type="text" id="genere" name="genere" value="${corso.genere}"/>

    <label>Livello:</label>
    <input type="text" id="livello" name="livello" value="${corso.livello}"/>

    <label>Categoria:</label>
    <input type="radio" id="danza" name="categoria" value="danza" <c:if test="${corso.categoria == 'Danza'}">checked</c:if> required>
    <label for="danza">Danza</label><br>
    <input type="radio" id="musica" name="categoria" value="musica" <c:if test="${corso.categoria == 'Musica'}">checked</c:if> required>
    <label for="musica">Musica</label><br>

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

    <br><label>Docenti attuali:</label>
    <ul>
        <c:forEach items="${sociInfo}" var="socio">
            <c:forEach items="${docenti}" var="docente">
                <c:if test="${docente.id == socio[3]}">
                    <li>${socio[1]} ${socio[2]} (${socio[0]}) - Stipendio: ${docente.stipendio}</li>
                </c:if>
            </c:forEach>
        </c:forEach>
    </ul>
    <p>Attenzione, è necessario reinserire il valore dello stipendio per ciuscun docente, prestando attenzione al fatto che esso non può essere inferiore a quello attualmente percepito</p>
    <label>Docenti:</label>

    <select name="docenti" id="dropdown" multiple onchange="updateDocentiSelection()">
        <c:forEach items="${sociInfo}" var="socio">
            <c:set var="isSelected" value="false"/>
            <c:forEach items="${docenti}" var="docente">
                <c:if test="${docente.id == socio[3]}">
                    <c:set var="isSelected" value="true"/>
                </c:if>
            </c:forEach>
            <option value="${socio[0]}" ${isSelected ? 'selected' : ''}>${socio[1]} ${socio[2]} (${socio[0]})</option>
        </c:forEach>
    </select><br>
    <div id="docentiStipendiContainer"></div><br>

    <br><label>Calendario attuale:</label>
    <ul>

        <c:forEach var="dayIndex" begin="1" end="5">
            <c:set var="giornoIt" value="${dayIndex == 1 ? 'Lunedì' : dayIndex == 2 ? 'Martedì' : dayIndex == 3 ? 'Mercoledì' : dayIndex == 4 ? 'Giovedì' : 'Venerdì'}"/>
            <c:set var="giorno" value="${dayIndex == 1 ? 'monday' : dayIndex == 2 ? 'tuesday' : dayIndex == 3 ? 'wednesday' : dayIndex == 4 ? 'thursday' : 'friday'}"/>
            <c:forEach items="${calendario}" var="selectedDay">
                <c:if test="${giorno == selectedDay.giornoSettimana}">
                <li>
                    Giorno: ${giornoIt} - Orario Inizio: ${selectedDay.orarioInizio}, Orario Fine: ${selectedDay.orarioFine}
                </li>
                </c:if>
            </c:forEach>
        </c:forEach>

    </ul>
    <label>Giorni di svolgimento:</label><br>
    <c:forEach var="dayIndex" begin="1" end="5">
        <c:set var="giornoIt" value="${dayIndex == 1 ? 'Lunedì' : dayIndex == 2 ? 'Martedì' : dayIndex == 3 ? 'Mercoledì' : dayIndex == 4 ? 'Giovedì' : 'Venerdì'}"/>
        <c:set var="giorno" value="${dayIndex == 1 ? 'monday' : dayIndex == 2 ? 'tuesday' : dayIndex == 3 ? 'wednesday' : dayIndex == 4 ? 'thursday' : 'friday'}"/>
        <label>${giornoIt}: <input type="checkbox" id="giorno${giorno}" name="giorni" value="${giorno}" /></label>
        <!--<c:forEach items="${calendario}" var="selectedDay">
            <c:if test="${giorno == selectedDay.giornoSettimana}">
                <script>document.getElementById('giorno${giorno}').checked = true;</script>
                <br>
                <label>Orario Inizio: <input type="time" name="orarioInizioAttuali" value="${selectedDay.orarioInizio}"/></label><br>
                <label>Orario Fine: <input type="time" name="orarioFineAttuali" value="${selectedDay.orarioFine}"/></label><br>
            </c:if>
        </c:forEach> -->
        <br>
    </c:forEach>
    <input type="submit" value="Aggiorna"/>
</form>
</body>
</html>
