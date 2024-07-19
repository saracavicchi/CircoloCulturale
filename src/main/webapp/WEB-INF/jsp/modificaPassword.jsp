<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 02/07/2024
  Time: 22:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Circolo La Sinfonia</title>

    <script>
        var errorDisplayed = false;
        window.onload = function() {
            // Aggiungi un listener per l'evento 'submit' al form
            document.getElementById('modificaPasswordForm').addEventListener('submit', submitForm);
            // Ottieni tutti gli elementi input del form
            var inputs = document.getElementById('modificaPasswordForm').getElementsByTagName('input');

            // Aggiungi un listener per l'evento 'input' a ogni elemento input
            for (var i = 0; i < inputs.length; i++) {
                inputs[i].addEventListener('focus', removeError);
            }
        }
        //campo/i che ha generato l'errore
        var erroredField = "";
        var errorMsg = "";

        function validateNewPassword() {
            var nuovaPassword = document.getElementById("newPassword").value;

            // Controlla se la password ha almeno 8 caratteri, almeno una lettera maiuscola, una lettera minuscola e un numero
            var passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
            if (!passwordRegex.test(nuovaPassword)) {
                errorMsg="La password deve avere almeno 8 caratteri, almeno una lettera maiuscola, una lettera minuscola e un numero.";
                erroredField="newPassword";
                return false;
            }

            return true;
        }
        function validateActualPassword() {
            var passwordAttualeDigitata = document.getElementById("currentPassword").value;
            var passwordAttualeReale = "${passwordAttuale}";

            if (passwordAttualeDigitata !== passwordAttualeReale) {
                errorMsg="La password inserita non corrisponde a quella attuale";
                erroredField="currentPassword";
                return false;
            }
            return true;
        }

        function submitForm(event) {
            event.preventDefault(); // Prevent the default form submission behavior
            // Validate the new password and the current password
            var newPasswordValidation =  validateNewPassword();
            var actualPasswordValidation = true;

            if("${segretario}" != 'true') {
                actualPasswordValidation = validateActualPassword();
            }

            // If validation fails, display error messages
            if (!newPasswordValidation || !actualPasswordValidation) {
                displayErrorMessages();
            } else {
                // If validation is successful, manually submit the form
                document.getElementById('modificaPasswordForm').submit();
            }
        }

        function displayErrorMessages() {
            errorDisplayed = true;
            var formElement = document.getElementById('modificaPasswordForm');

            // Check if the general error message exists, if not, create it
            var errorMessageElement = document.getElementById('error-message');
            if (!errorMessageElement) {
                errorMessageElement = document.createElement('p');
                errorMessageElement.id = 'error-message';
                errorMessageElement.textContent = "Errore durante l'inserimento, si prega di correggere le informazioni errate.";
                formElement.appendChild(errorMessageElement);
            }

            // Check if the specific error message exists, if not, create it
            var specificErrorElement = document.getElementById('specific-error');
            if (!specificErrorElement) {
                specificErrorElement = document.createElement('p');
                specificErrorElement.id = 'specific-error';
                specificErrorElement.textContent = errorMsg;
                formElement.appendChild(specificErrorElement);
            }

            // Highlight errored fields
            var fields = erroredField.split(', ');
            for (var i = 0; i < fields.length; i++) {
                var fieldElement = document.getElementById(fields[i]);
                fieldElement.style.border = '1px solid red';
            }

            // Scroll to the h1 element
            errorMessageElement.scrollIntoView({behavior: "smooth"});
        }

        function removeError() {
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

            // Ottieni tutti gli elementi input del form
            var inputs = document.getElementById('modificaPasswordForm').getElementsByTagName('input');

            // Itera su ogni elemento input
            for (var i = 0; i < inputs.length; i++) {
                // Rimuovi il bordo rosso dal campo
                inputs[i].style.border = '';
            }
        }
    </script>
</head>
<body>
<h1>Modifica Password</h1>
<form id="modificaPasswordForm" name="modificaPasswordForm" action="modificaPassword" method="POST" >
    <input type="hidden" name="socioId" value="${socioId}" />

    <input type="hidden" name="segretario" value="${segretario}" />

    <c:if test="${segretario != 'true'}">
        <label for="currentPassword">Password Attuale:</label>
        <input type="password" id="currentPassword" name="currentPassword" placeholder="Password Attuale" required>
    </c:if>

    <label for="newPassword">Nuova Password:</label>
    <input type="password" id="newPassword" name="newPassword" placeholder="Nuova Password" required>

    <button type="submit" name="confirm" value="Conferma">Conferma</button>
</form>
<%@include file="/static/include/footer.jsp"%>
</body>
</html>
