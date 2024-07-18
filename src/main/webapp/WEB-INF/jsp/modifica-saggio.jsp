<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 11/07/2024
  Time: 18:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Circolo Culturale</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css">

    <script>
        var errorDisplayed = false;
        document.addEventListener('DOMContentLoaded', function() {
            initializeAddressFields();
            initModificaSaggioForm();
            // Disabilita gli elementi del form all'avvio
            toggleFormElements(false);
            var saggioDeleted = '${saggio.deleted}';
            if(saggioDeleted === "false") {
                document.getElementById('enableEdit').addEventListener('change', function() {
                    toggleFormElements(this.checked);
                });
            }
            //handleEliminaSaggioFormSubmission()

        });
        /*
        //inutile con required nel checkbox
        function handleEliminaSaggioFormSubmission() {

            document.getElementById('deletionForm').addEventListener('submit', confirmEliminazione);
        }

        function confirmEliminazione(event) {
            var confirmUnsubscribe = document.getElementById('confirmDeletion');
            if (!confirmUnsubscribe.checked) {
                alert('Per favore, conferma se sei sicuro di voler cancellare il saggio');
                event.preventDefault(); // Prevent form submission
            }
        }

         */
        function toggleFormElements(isEnabled) {
            var formElements = document.getElementById('modificaSaggioForm').elements;
            for (var i = 0; i < formElements.length; i++) {
                // Evita di disabilitare il checkbox stesso
                if (formElements[i].id !== 'enableEdit') {
                    formElements[i].disabled = !isEnabled;
                }
            }
        }

        function initModificaSaggioForm() {
            var modificaSaggioForm = document.getElementById('modificaSaggioForm');
            if (modificaSaggioForm) {
                modificaSaggioForm.addEventListener('submit', submitForm);
                var inputs = modificaSaggioForm.getElementsByTagName('input');
                addFocusListenersToInputs(inputs, 'modificaSaggioForm');
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
                errorMsg = "Descrizione contiene alcuni caratteri non validi";
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
                document.getElementById('modificaSaggioForm').submit();
            }
        }

        function displayErrorMessages() {
            var formElement = document.getElementById('modificaSaggioForm');
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

        function divideIndirizzo(saggio) {
            const parti = saggio.indirizzo.split(', ');
            const risultato = {
                state: parti[0],
                province: parti[1],
                city: parti[2],
                street: parti[3],
                houseNumber: parti[4]
            };
            console.log(risultato)
            return risultato;
        }

        function initializeAddressFields() {
            var saggio = {
                indirizzo: "${saggio.indirizzo}"
            };
            var risultato = divideIndirizzo(saggio);
            document.getElementById('stato').value = risultato.state;
            document.getElementById('provincia').value = risultato.province;
            document.getElementById('citta').value = risultato.city;
            document.getElementById('via').value = risultato.street;
            document.getElementById('numeroCivico').value = risultato.houseNumber;
        }
    </script>
</head>
<body>
<%@ include file="/static/include/header.jsp" %>
<div id="main-content" class="clearfix">
    <main class="midleft">
        <section class="title">
            <h1>Modifica le informazioni del saggio "${saggio.nome}"</h1>
        </section>
        <section class="content">

            <img class="profile-image" src="${empty saggio.urlFoto ? uploadDir.concat(placeholderImage) : uploadDir.concat(saggio.urlFoto)}" alt="Foto saggio"/>

            <c:if test="${not saggio.deleted}">
            <label for="enableEdit">Modifica abilitata:</label>
            <input type="checkbox" id="enableEdit" name="enableEdit">
            </c:if>

            <form id="modificaSaggioForm" name="modificaSaggioForm" action="/saggio/modifica" method="post" enctype="multipart/form-data">
                <input type="hidden" name="saggioId" value="${saggio.id}"/>

                <fieldset>
                    <legend>Informazioni Principali</legend>
                    <label for="photo">Seleziona una nuova foto per il saggio:</label>
                    <input type="file" id="photo" name="photo">

                    <label for="nome">Nome:</label>
                    <input type="text" id="nome" name="nome" required maxlength="30" value="${saggio.nome}">

                    <label for="descrizione">Descrizione:</label>
                    <input type="text" id="descrizione" name="descrizione" value="${not empty saggio.descrizione ? saggio.descrizione : ''}" placeholder="Inserire descrizione"/>

                    <label for="data">Data:</label>
                    <input type="date" id="data" name="data" required value="${saggio.data}">

                    <label for="numeroPartecipanti">Numero massimo partecipanti:</label>
                    <input type="number" id="numeroPartecipanti" name="numeroPartecipanti" required min="1" value="${saggio.maxPartecipanti}">
                </fieldset>

                <fieldset>
                    <legend>Orari</legend>
                    <label for="orarioInizio">Orario inizio:</label>
                    <input type="time" id="orarioInizio" name="orarioInizio" value="${not empty saggio.orarioInizio ? saggio.orarioInizio : ''}" placeholder="HH:MM">

                    <label for="orarioFine">Orario fine:</label>
                    <input type="time" id="orarioFine" name="orarioFine" value="${not empty saggio.orarioFine ? saggio.orarioFine : ''}" placeholder="HH:MM">
                </fieldset>

                <fieldset>
                    <legend>Indirizzo</legend>
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
                <c:if test="${not saggio.deleted}">
                    <fieldset>
                        <legend>Corsi Associati</legend>
                        <label for="corsi">Seleziona Corsi:</label>
                        <select id="corsi" name="corsi" multiple>
                            <c:forEach items="${corsi}" var="corso">
                                <c:choose>
                                    <c:when test="${saggio.corsi.contains(corso)}">
                                        <option value="${corso.id}" selected>${corso.categoria}, ${corso.genere}, ${corso.livello}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${corso.id}">${corso.categoria}, ${corso.genere}, ${corso.livello}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </fieldset>
                </c:if>
                <button type="submit">Modifica Saggio</button>
            </form>
        </section>
        <c:if test="${not saggio.deleted}">
            <section class="content">
                <div class="custom-fieldset">
                    <h1 class="custom-legend">Cancellazione saggio</h1>
                    <form id="deletionForm" action="/saggio/elimina" method="POST">
                        <input type="hidden" name="saggioId" value="${saggio.id}" />
                        <label for="confirmDeletion">Sei sicuro?</label>
                        <input type="checkbox" id="confirmDeletion" name="confirmDeletion" required>
                        <button type="submit">Elimina saggio</button>
                    </form>
                </div>
            </section>
        </c:if>

    </main>
<%@include file="/static/include/aside.jsp"%>
</div>
<%@include file="/static/include/footer.jsp"%>
</body>
</html>
