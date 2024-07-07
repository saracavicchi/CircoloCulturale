<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 02/07/2024
  Time: 20:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Profilo Socio</title>
    <style>
        .profile-pic {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            object-fit: cover;
        }
        form {
            display: flex;
            flex-direction: column;
            width: 300px;
        }
        form input, form button {
            margin: 5px 0;
        }
    </style>
    <script>
        var errorDisplayed =false;
        function divideIndirizzo(utente) {
            const parti = utente.indirizzo.split(', ');
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
        document.addEventListener('DOMContentLoaded', function() {
            initializeAddressFields();
            initProfileForm();
            checkSegretarioAndSetupForm();
            handleEliminaSocioFormSubmission();
            var failSocioMod = "${param.failSocioMod}";
            if (failSocioMod == 'true') {
                scrollToErrorMsg();
            }
        });

        function initializeAddressFields() {
            var utente = {
                indirizzo: "${utente.indirizzo}"
            };
            var risultato = divideIndirizzo(utente);
            document.getElementById('state').value = risultato.state;
            document.getElementById('province').value = risultato.province;
            document.getElementById('city').value = risultato.city;
            document.getElementById('street').value = risultato.street;
            document.getElementById('houseNumber').value = risultato.houseNumber;
        }

        function handleEliminaSocioFormSubmission() {

            document.getElementById('modificaCredenziali').addEventListener('submit', confirmDisiscrizione);
        }

        function scrollToErrorMsg() {
            var ErrorMsgElement = document.getElementById('ErroreMsg');
            if (ErrorMsgElement) {
                ErrorMsgElement.scrollIntoView({behavior: "smooth"});
            }
        }

        function confirmDisiscrizione(event) {
            var confirmUnsubscribe = document.getElementById('confirmUnsubscribe');
            if (!confirmUnsubscribe.checked) {
                alert('Per favore, conferma se sei sicuro di volerti disiscrivere.');
                event.preventDefault(); // Prevent form submission
            }
        }


        function initProfileForm() {
            var profileForm = document.getElementById('profileForm');
            if (profileForm) {
                profileForm.addEventListener('submit', submitForm);
                var inputs = profileForm.getElementsByTagName('input');
                addFocusListenersToInputs(inputs, 'profileForm');
            }
        }

        function checkSegretarioAndSetupForm() {
            var socioModificaForm = document.getElementById('socioModificaForm');
            if (socioModificaForm) {
                socioModificaForm.addEventListener('submit', submitSegretarioForm);
                var inputs = socioModificaForm.getElementsByTagName('input');
                addFocusListenersToInputs(inputs, 'socioModificaForm');
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
            var form = document.profileForm;
            var cf = form.cf.value;
            var name = form.name.value;
            var surname = form.surname.value;
            var dob = form.dob.value;
            var birthplace = form.birthplace.value;
            var state = form.state.value;
            var email = form.email.value;
            var province = form.province.value;
            var city = form.city.value;
            var street = form.street.value;
            var houseNumber = form.houseNumber.value;
            var phoneNumber = form.phoneNumber.value;

            // Controlla se il nome, cognome, luogo di nascita, stato, provincia, città, via contengono solo caratteri e non numeri
            var regex = /^[A-Za-z\s\'\-]+$/;
            if (!regex.test(name) || !regex.test(surname) || !regex.test(birthplace) || !regex.test(state) || !regex.test(province) || !regex.test(city) || !regex.test(street)) {
                errorMsg = "I campi nome, cognome, luogo di nascita, stato, provincia, città, via devono contenere solo caratteri e non numeri.";
                erroredField = "name, surname, birthplace, state, province, city, street";
                return false;
            }

            // Controlla se la data di nascita è una data o del giorno corrente o antecedente
            var today = new Date();
            today.setHours(0, 0, 0, 0);
            var inputDate = new Date(dob);
            if (inputDate > today) {
                errorMsg = "La data di nascita deve essere odierna o antecedente.";
                erroredField = "dob";
                return false;
            }

            // Controlla se la somma dei caratteri di stato, provincia, città, via e numero civico non supera gli 80 caratteri
            if ((state.length + province.length + city.length + street.length + houseNumber.length) > 80) {
                errorMsg = "La somma dei caratteri di stato, provincia, città, via e numero civico non deve superare gli 80 caratteri.";
                erroredField = "state, province, city, street, houseNumber";
                return false;
            }

            // Controlla CF
            if (!validateCF(cf)) {
                erroredField = "cf";
                return false;
            }

            // Controlla se l'email è un'email valida
            var emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (!emailRegex.test(email)) {
                errorMsg = "Inserisci un'email valida.";
                errorField = "email";
                return false;
            }


            // Controlla se il numero di telefono contiene solo numeri
            var phoneRegex = /^[0-9]{10}$/;
            if (phoneNumber && !phoneRegex.test(phoneNumber)) {
                errorMsg="Il numero di telefono deve contenere solo 10 numeri.";
                erroredField="phoneNumber";
                return false;
            }


            // Se tutti i controlli passano, restituisce true per permettere l'invio del form
            return true;
        }

        function validateCF(cf){
            // Controlla se il codice fiscale è composto sia da numeri che da lettere
            var cfRegex = /^[0-9a-zA-Z]{16}$/;
            if (!cfRegex.test(cf)) {
                errorMsg = "Il codice fiscale deve essere di 16 caratteri e composto sia da numeri che da lettere.";
                return false;
            }
            return true;
        }

        function submitSegretarioForm(event) {
            // Impedisci l'invio del form
            event.preventDefault();

            var cf= document.getElementById('cfSocio').value;

            // Chiama la funzione validateForm
            var validation = validateCF(cf);

            if (!validation) {
                erroredField = "cfSocio";

                var Element = document.getElementById('modificaCredenziali');
                displayErrorMessages(Element);

            } else {  // Se la validazione ha esito positivo, invia il form
                // Usa l'ID del form per inviarlo direttamente
                document.getElementById('socioModificaForm').submit();
            }
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
                document.getElementById('profileForm').submit();
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
            var fields = erroredField.split(', ');
            for (var i = 0; i < fields.length; i++) {
                var fieldElement = document.getElementById(fields[i]);
                if (fieldElement) {
                    fieldElement.style.border = '1px solid red';
                }
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

            // Ottieni tutti gli elementi input del form
            var inputs = document.getElementById(formName).getElementsByTagName('input');

            // Itera su ogni elemento input
            for (var i = 0; i < inputs.length; i++) {
                // Rimuovi il bordo rosso dal campo
                inputs[i].style.border = '';
            }
        }



    </script>
</head>
<body>
<h1>Ciao, ${utente.nome}, ecco le tue informazioni personali</h1>
<div>
    <img src="${empty socio.urlFoto ? placeholderImagePath : socio.urlFoto}" alt="Foto Profilo" class="profile-pic"/>
</div>
<form id="profileForm" name="profileForm" action="socioProfile" method="POST" enctype="multipart/form-data">
    <input type="hidden" name="socioId" value="${socio.id}" />

    <label for="photo">Seleziona una nuova Foto Profilo:</label>
    <input type="file" id="photo" name="photo">

    <label for="name">Nome:</label>
    <input type="text" id="name" name="name" value="${utente.nome}" placeholder="Nome" />

    <label for="surname">Cognome:</label>
    <input type="text" id="surname" name="surname" value="${utente.cognome}" placeholder="Cognome" />

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" value="${socio.email}" placeholder="Email" />

    <label for="cf">Codice Fiscale:</label>
    <input type="text" id="cf" name="cf" value="${utente.cf}" placeholder="Codice Fiscale" />

    <label for="dob">Data di nascita:</label>
    <input type="date" id="dob" name="dob" value="${utente.dataNascita}" placeholder="Data di nascita" />

    <label for="birthplace">Luogo di nascita (città):</label>
    <input type="text" id="birthplace" name="birthplace" value="${utente.luogoNascita}" placeholder="Luogo di nascita (città)" />

    <label for="state">Stato:</label>
    <input type="text" id="state" name="state" placeholder="Stato" />

    <label for="province">Provincia:</label>
    <input type="text" id="province" name="province" placeholder="Provincia" />

    <label for="city">Città:</label>
    <input type="text" id="city" name="city" placeholder="Città" />

    <label for="street">Via:</label>
    <input type="text" id="street" name="street" placeholder="Via" />

    <label for="houseNumber">Numero Civico:</label>
    <input type="text" id="houseNumber" name="houseNumber" placeholder="Numero Civico" />

    <label for="phoneNumber">Numero di Telefono:</label>
    <input type="text" id="phoneNumber" name="phoneNumber" value="${socio.telefono}" placeholder="Numero di Telefono" />
    <button type="submit">Aggiorna</button>
</form>

<form action="modificaPassword" method="GET">
    <input type="hidden" name="socioIdPassword" value="${socio.id}" />
    <input type="hidden" name="segretario" value="${segretario}" />
    <button type="submit" >Modifica Password</button>
</form>
<c:if test="${param.failSocioMod == 'true'}">
    <p id="ErroreMsg" >Errore nell'operazione, riprovare</p>
</c:if>

<p>Disiscrizione dal Circolo Culturale</p>
<form action="eliminaSocio" method="POST">
    <input type="hidden" name="socioIdElimina" value="${socio.id}" />
    <input type="hidden" name="segretario" value="${segretario}" />
    <label for="confirmUnsubscribe">Sei sicuro?</label>
    <input type="checkbox" id="confirmUnsubscribe" name="confirmUnsubscribe" required>
    <button type="submit">Disiscriviti</button>
</form>



<c:if test="${segretario == 'true'}">
<p id="modificaCredenziali">Inserisci le credenziali per modificare le informazioni di un socio:</p>

<form id="socioModificaForm" name="socioModificaForm" action="segretarioModificaSocio" method="post">

    <input type="hidden" name="socioId" value="${socio.id}" />

    <label for="cfSocio">Codice Fiscale:</label>
    <input type="text" id="cfSocio" name="cfSocio" placeholder="Codice Fiscale" required>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" placeholder="Password" required>

    <button type="submit">Invia</button>
</form>
</c:if>

</body>
</html>
