<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 13/07/2024
  Time: 12:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Circolo La Sinfonia</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css">
    <script>
        var errorDisplayed = false;

        function redirectToEditSedePage() {
            var idSede = '<%= request.getParameter("idSede") %>';
            window.location.href = '/sede/modifica?idSede=' + idSede;
        }

        document.addEventListener('DOMContentLoaded', function() {

            // Inizializza tutti i form come disabilitati
            document.querySelectorAll('[id^="enableEdit_"]').forEach(function(checkbox) {
                toggleFormElements(checkbox.checked, checkbox.getAttribute('id').split('_')[1]);
                checkbox.addEventListener('change', function() {
                    toggleFormElements(this.checked, this.getAttribute('id').split('_')[1]);
                });
            });
            initModificaSalaForm();

        });

        function toggleFormElements(isEnabled, salaId) {
            var form = document.getElementById('modificaSaleForm_' + salaId);
            if (form) {
                var formElements = form.elements;
                for (var i = 0; i < formElements.length; i++) {
                    formElements[i].disabled = !isEnabled;
                }
            }
        }
        function initModificaSalaForm() {
            //seleziona tutti i form il cui id comincia con 'modificaSaleForm_'
            var modificaSalaForms = document.querySelectorAll('form[id^="modificaSaleForm_"]');

            modificaSalaForms.forEach(function(form) {
                // 'submit' event listener ad ogni fom form
                console.log(form.getAttribute('id'), form.getAttribute('id').split('_')[1]);
                form.addEventListener('submit', function(event) {
                    submitForm(form.getAttribute('id').split('_')[1], event);
                });

            });
        }

        //campo/i che ha generato l'errore
        var erroredField = "";
        var errorMsg = "";

        function validateForm(salaId) {
            console.log(salaId)
            console.log('numeroSala_' + salaId);
            console.log(document.getElementById('numeroSala_' + salaId));
            var numeroSalaElement = document.getElementById('numeroSala_' + salaId);
            var numeroSala =numeroSalaElement.value;
            console.log(numeroSala);
            var descrizioneElement = document.getElementById('descrizione_' + salaId);
            var descrizione = descrizioneElement.value;
            console.log(numeroSala,descrizione)
            var charDescrizioneRegex = /^(?=.*[A-Za-z])[A-Za-z\s\'\-\(\)\.\,\;\:\!\?\[\]\{\}\"\-àèéìòùÀÈÉÌÒÙáéíóúÁÉÍÓÚâêîôûÂÊÎÔÛäëïöüÿÄËÏÖÜŸ]+$/;

            if (descrizione && !descrizione.match(charDescrizioneRegex)) {
                errorMsg = "Descrizione contiene caratteri non validi";
                return false;
            }

            if (!numeroSala || !/^\d+$/.test(numeroSala)) {
                errorMsg="Il numero della sala deve essere presente e può contenere solo numeri."
                return false;
            }

            return true; // La validazione è passata
        }

        function submitForm(salaId, event) {

            if(event) event.preventDefault();

            var formId = 'modificaSaleForm_' + salaId;
            var form = document.getElementById(formId);

            var validation = validateForm(salaId);

            if (!validation) {
                //seleziona tutti input della form
                var inputs = form.querySelectorAll('input, textarea, select');
                console.log(form)

                //'focus' event listener a ogni input element
                inputs.forEach(function(input) {
                    console.log(input)
                    input.addEventListener('focus', function() {
                        removeError();
                    });
                });
                displayErrorMessages();
            } else {

                form.submit();
            }
        }

        function displayErrorMessages() {
            errorDisplayed = true;
            console.log(errorDisplayed)
            var errorHere= document.getElementById('errorHere');
            // Controlla se il messaggio di errore esiste già
            var errorMessageElement = document.getElementById('error-message');
            var specificErrorElement = document.getElementById('specific-error');

            // Se il messaggio di errore non esiste, crealo
            if (!errorMessageElement) {
                errorMessageElement = document.createElement('p');
                errorMessageElement.id = 'error-message';
                errorMessageElement.textContent = "Errore durante l'inserimento, si prega di correggere le informazioni errate.";
                document.getElementById('errorHere').insertAdjacentElement('afterbegin', errorMessageElement);
            }

            // Se il messaggio di errore specifico non esiste, crealo
            if (!specificErrorElement) {
                specificErrorElement = document.createElement('p');
                specificErrorElement.id = 'specific-error';
                specificErrorElement.textContent = errorMsg;
                document.getElementById('errorHere').insertAdjacentElement('afterbegin', specificErrorElement);

            }
            if(errorMessageElement) {
                scrollToErrorMsg();
            }

        }
        function scrollToErrorMsg() {
            var ErrorMsgElement = document.getElementById('specific-error');
            if (ErrorMsgElement) {
                ErrorMsgElement.scrollIntoView({behavior: "smooth"});
            }
        }

        function removeError() {
            console.log(errorDisplayed)
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
        }


    </script>
</head>
<body>
<%@ include file="/static/include/header.jsp" %>
<div id="main-content" class="clearfix">
    <main class="midleft">
        <section class="title">
            <h1>Modifica Sale</h1>
        </section>
        <section id="errorHere" class="content">

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
        <c:forEach items="${sale}" var="sala">
            <div class="custom-fieldset">
                <h1 class="custom-legend">Modifica Sala ${sala.numeroSala}</h1>

                <label style="color:var(--border); margin-left:35px" for="enableEdit_${sala.id}">Modifica abilitata:</label>
                <input type="checkbox" id="enableEdit_${sala.id}" name="enableEdit">

                <form id="modificaSaleForm_${sala.id}" name="modificaSaleForm" action="/sede/sala/modifica" method="post">
                    <input type="hidden" name="idSala" value="${sala.id}" />
                    <input type="hidden" name="idSede" value="${sala.idSede.id}" />

                    <label for="numeroSala_${sala.id}">Numero Sala:</label>
                    <input type="text" id="numeroSala_${sala.id}" name="numero" value="${sala.numeroSala}" required />

                    <label for="descrizione_${sala.id}">Descrizione:</label>
                    <textarea id="descrizione_${sala.id}" name="descrizione">${sala.descrizione}</textarea>

                    <label for="prenotabile_${sala.id}">Prenotabile:</label>
                    <select id="prenotabile_${sala.id}" name="prenotabile">
                        <option value="true" ${sala.prenotabile ? 'selected' : ''}>Sì</option>
                        <option value="false" ${!sala.prenotabile ? 'selected' : ''}>No</option>
                    </select>

                    <label>Capienza: ${sala.capienza}</label>

                    <button type="submit">Salva Modifiche per la sala ${sala.numeroSala}</button>
                </form>
            </div>
            <div class="custom-fieldset">
                <h1 class="custom-legend">Cancellazione Sala</h1>
                <form id="cancellaSalaForm_${sala.id}" action="/sede/sala/elimina" method="POST">
                    <input type="hidden" name="idSala" value="${sala.id}" />
                    <input type="hidden" name="idSede" value="${sala.idSede.id}" />
                    <label for="confirmDeletion_${sala.id}">Sei sicuro?</label>
                    <input type="checkbox" id="confirmDeletion_${sala.id}" name="confirmDeletion" required>
                    <button type="button" onclick="document.getElementById('cancellaSalaForm_${sala.id}').submit();">Cancella Sala</button>
                </form>
            </div>
        </c:forEach>
        </section>
        <section class="content">
            <button type="button" onclick="redirectToEditSedePage()">Modifica Sede</button>
        </section>
    </main>
    <%@include file="/static/include/aside.jsp"%>
</div>
<%@include file="/static/include/footer.jsp"%>
</body>
</html>
