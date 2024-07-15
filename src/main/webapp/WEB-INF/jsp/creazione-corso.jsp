<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 05/07/2024
  Time: 08:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Crea Corso</title>
    <script>
        var errorDisplayed = false;

        document.addEventListener('DOMContentLoaded', function() {

            initCreaCorsoForm();
            updateDocentiSelection();
            /*var fail = "${param.fail}";
            if (fail == 'true') {
                scrollToErrorMsg();
            }*/

        });


        /*function scrollToErrorMsg() {
            var ErrorMsgElement = document.getElementById('ErroreMsg');
            if (ErrorMsgElement) {
                ErrorMsgElement.scrollIntoView({behavior: "smooth"});
            }
        }*/
        function initCreaCorsoForm() {
            var creaCorsoForm = document.getElementById('creaCorsoForm');
            if (creaCorsoForm) {
                creaCorsoForm.addEventListener('submit', submitForm);
                var inputs = creaCorsoForm.getElementsByTagName('input');
                addFocusListenersToInputs(inputs, 'creaCorsoForm');
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

            if (!validation) {
                // Ottieni l'elemento h1
                var h1Element = document.getElementsByTagName('h1')[0];
                displayErrorMessages(h1Element);
            } else {  // Se la validazione ha esito positivo, invia il form
                // Usa l'ID del form per inviarlo direttamente
                document.getElementById('creaCorsoForm').submit();
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
            if (erroredField != "") {
                var fields = erroredField.split(', ');
                for (var i = 0; i < fields.length; i++) {
                    var fieldElement = document.getElementById(fields[i]);
                    if (fieldElement) {
                        fieldElement.style.border = '1px solid red';
                    }
                }
                if (errorMessageElement) {
                    scrollToErrorMsg();
                }

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

        function mapDayNumberToWeekday(dayNumber) {
            var weekdays = {
                1: 'Lunedì',
                2: 'Martedì',
                3: 'Mercoledì',
                4: 'Giovedì',
                5: 'Venerdì'
            };
            return weekdays[dayNumber] || 'Unknown Day';
        }

        function toggleTimeInputs(checkbox, giorno) {
            var divId = 'orario' + giorno;
            var divOrario = document.getElementById(divId);

            if (checkbox.checked) {
                divOrario.style.display = 'block';
            } else {
                divOrario.style.display = 'none';
            }
        }



        function updateDocentiSelection() {
            mostraDocentiSelezionati();
            gestisciStipendiDocenti();
        }

        function mostraDocentiSelezionati() {
            var selectDocenti = document.getElementsByName('docenti')[0];
            var selectedDocentiContainer = document.getElementById('selectedDocentiContainer');
            selectedDocentiContainer.innerHTML = ''; // Pulisce la lista corrente

            Array.from(selectDocenti.selectedOptions).forEach(function(option) {
                console.log("Opzione selezionata: ", option.text, " Valore: ", option.value);
                var docenteElement = document.createElement('div');
                docenteElement.textContent = option.text;
                selectedDocentiContainer.appendChild(docenteElement);
            });
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
                docenteStipendio.setAttribute('title', 'Per favore inserire un importo corretto senza cifre decimali.');
                docenteStipendio.setAttribute('required', '');
                docenteStipendio.setAttribute('min', '10000');
                docenteStipendio.setAttribute('max', '100000');

                docenteDiv.appendChild(docenteLabel);
                docenteDiv.appendChild(docenteStipendio);
                docentiStipendiContainer.appendChild(docenteDiv);
            });
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
<h1>Crea un corso</h1>
<% String fail;
    if ((fail = request.getParameter("fail")) != null && fail.equals("true")) {
%>
<p>Errore durante la creazione del corso, verificare le informazioni e riprovare</p>
<%
    }
%>
<form id="creaCorsoForm" action="crea" method="post" enctype="multipart/form-data">

    Descrizione: <input type="text" id="descrizione" name="descrizione" required><br>
    Genere: <input type="text" id="genere" name="genere" required><br>
    Livello: <input type="text" id="livello" name="livello" required><br>
    <!-- Categoria: <input type="text" id="categoria" name="categoria" required><br> -->
    Categoria:
    <input type="radio" id="danza" name="categoria" value="Danza" required>
    <label for="danza">Danza</label><br>
    <input type="radio" id="musica" name="categoria" value="Musica" required>
    <label for="musica">Musica</label><br>


    Sala: <select name="idSala" required>
    <c:set var="currentSedeId" value="" />
    <c:forEach items="${sale}" var="sala">
        <c:choose>
            <c:when test="${not currentSedeId.equals(sala.idSede.id)}">
                <optgroup label="Nome Sede: ${sala.idSede.nome}">
                <c:set var="currentSedeId" value="${sala.idSede.id}" />
            </c:when>
            <c:otherwise></c:otherwise>
        </c:choose>
        <option value="${sala.id}">${sala.numeroSala}</option>
        <c:if test="${sale.get(sale.size() -1) == sala}">
            </optgroup>
        </c:if>
        <c:if test="${sale.indexOf(sala) == sale.size() - 1}">
            </optgroup>
        </c:if>
    </c:forEach>
    </select><br>
    <label for="photo">Seleziona una foto per il corso:</label>
    Foto: <input type="file" id="photo" name="photo" enctype="multipart/form-data"><br>

    <p>Docenti selezionati:</p>
    <div id="selectedDocentiContainer"></div>
    Docenti: <select name="docenti" multiple onchange="updateDocentiSelection()">
    <c:forEach items="${sociInfo}" var="socioD">
        <option value="${socioD[0]}">${socioD[1]} ${socioD[2]} (${socioD[0]})</option>
    </c:forEach>
    </select>
    <p>Attenzione, gli stipendi dei docenti verranno aggiornati solamente nel caso l'importo risulti superiore a quello attualmente percepito</p>
    <div id="docentiStipendiContainer"></div>

    Calendario Settimanale: <br>
    <c:forEach items="${giorniSettimana}" var="giorno">
        <div>
            <script>document.write(mapDayNumberToWeekday(${giorno}) + ':');</script> <input type="checkbox" name="giorni" value="${giorno}" onchange="toggleTimeInputs(this,'${giorno}');"><br>
            <div id="orario${giorno}" style="display:none;">
                Orario Inizio: <input type="time" name="orariInizio"><br>
                Orario Fine: <input type="time" name="orariFine"><br>
            </div>
        </div>
    </c:forEach>
    <input type="submit" value="Crea Corso">
</form>
</body>
</html>
