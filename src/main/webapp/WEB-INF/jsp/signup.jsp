<%--
  Creato da IntelliJ IDEA.
  Utente: saracavicchi
  Data: 22/06/2024
  Ora: 12:18
  Per modificare questo modello usa File | Impostazioni | Modelli di file.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>CircoloCulturale</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css">
    <style>
        .error-message,
        .specific-error {
            color: red;
            text-align: center;
            margin-top: 20px;
        }

        .registered {
            color: #FF0000;
            font-size: 20px;
            text-align: center;
        }
    </style>
    <script>
        var errorDisplayed =false;
        window.onload = function() {
            // Aggiungi un listener per l'evento 'submit' al form
            document.getElementById('registrationForm').addEventListener('submit', submitForm);
            // Ottieni tutti gli elementi input del form
            var inputs = document.getElementById('registrationForm').getElementsByTagName('input');

            // Aggiungi un listener per l'evento 'input' a ogni elemento input
            for (var i = 0; i < inputs.length; i++) {
                inputs[i].addEventListener('focus', removeError);
            }

            var urlParams = new URLSearchParams(window.location.search);
            var success = urlParams.get('success');
            var cancelled = urlParams.get('cancelled');
            var error = urlParams.get('error');
            if (success) {
                var successElement = document.createElement('h2');
                successElement.textContent = "Registrazione avvenuta con successo!";
                successElement.style.color = 'green';
                successElement.style.textAlign = 'center';
                document.getElementsByTagName('h1')[0].appendChild(successElement);
            } else if (cancelled || error) {
                var cancelledElement = document.createElement('h2');
                cancelledElement.textContent = "Pagamento fallito, rivolgersi alla segreteria con l'identificativo Tessera mandato per email";
                cancelledElement.style.color = 'red';
                cancelledElement.style.textAlign = 'center';
                document.getElementsByTagName('h1')[0].appendChild(cancelledElement);
            }
        }

        //campo/i che ha generato l'errore
        var erroredField = "";
        var errorMsg = "";

        function validateForm() {
            var form = document.registrationForm;
            var cf = form.cf.value;
            var name = form.name.value;
            var surname = form.surname.value;
            var dob = form.dob.value;
            var birthplace = form.birthplace.value;
            var country = form.country.value;
            var province = form.province.value;
            var city = form.city.value;
            var street = form.street.value;
            var houseNumber = form.houseNumber.value;
            var email = form.email.value;
            var password = form.password.value;
            var phoneNumber = form.phoneNumber.value;
            //var photoUrl = form.photoUrl.value;

            // Controlla se il nome, cognome, luogo di nascita, stato, provincia, città, via contengono solo caratteri e non numeri
            var regex = /^(?=.*[A-Za-z])[A-Za-z\s\'\-]+$/;
            if (!regex.test(name) || !regex.test(surname) || !regex.test(birthplace) || !regex.test(country) || !regex.test(province) || !regex.test(city) || !regex.test(street)) {
                errorMsg = "I campi nome, cognome, luogo di nascita, stato, provincia, città, via devono contenere solo caratteri e non numeri.";
                erroredField = "name, surname, birthplace, country, province, city, street";
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
            if ((country.length + province.length + city.length + street.length + houseNumber.length) > 80) {
                errorMsg = "La somma dei caratteri di stato, provincia, città, via e numero civico non deve superare gli 80 caratteri.";
                erroredField = "country, province, city, street, houseNumber";
                return false;
            }

            // Controlla se il codice fiscale è composto sia da numeri che da lettere
            var cfRegex = /^[0-9a-zA-Z]+$/;
            if (!cfRegex.test(cf)) {
                errorMsg = "Il codice fiscale deve essere composto sia da numeri che da lettere.";
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

            // Controlla se la password ha almeno 8 caratteri, almeno una lettera maiuscola, una lettera minuscola e un numero
            var passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
            if (!passwordRegex.test(password)) {
                errorMsg="La password deve avere almeno 8 caratteri, almeno una lettera maiuscola, una lettera minuscola e un numero.";
                erroredField="password";
                return false;
            }

            // Se tutti i controlli passano, restituisce true per permettere l'invio del form
            return true;
        }

        function submitForm(event) {
            // Impedisci l'invio del form
            event.preventDefault();

            // Chiama la funzione validateForm
            var validation = validateForm();

            // Se la validazione ha esito positivo, invia il form
            if (validation) {
                event.target.submit();
            } else {
                errorDisplayed = true;
                // Ottieni l'elemento h1
                var formElement = document.getElementById('registrationForm');

                // Controlla se il messaggio di errore esiste già
                var errorMessageElement = document.getElementById('error-message');
                var specificErrorElement = document.getElementById('specific-error');

                // Se il messaggio di errore non esiste, crealo
                if (!errorMessageElement) {
                    errorMessageElement = document.createElement('p');
                    errorMessageElement.style.color = 'red';
                    errorMessageElement.id = 'error-message';
                    errorMessageElement.textContent = "Errore durante l'inserimento, si prega di correggere le informazioni errate.";
                    document.querySelector('.content').insertBefore(errorMessageElement, formElement);
                }

                // Se il messaggio di errore specifico non esiste, crealo
                if (!specificErrorElement) {
                    specificErrorElement = document.createElement('p');
                    specificErrorElement.id = 'specific-error';
                    errorMessageElement.style.color = 'red';
                    specificErrorElement.textContent = errorMsg;
                    document.querySelector('.content').insertBefore(specificErrorElement, formElement);
                }

                // Colora il bordo del campo o dei campi che hanno dato errore
                var fields = erroredField.split(', ');
                for (var i = 0; i < fields.length; i++) {
                    var fieldElement = document.getElementById(fields[i]);
                    fieldElement.style.border = '1px solid red';
                }

                // Fai scorrere la pagina fino all'elemento h1
                h1Element.scrollIntoView({behavior: "smooth"});
            }
        }

        function removeError(event) {
            if(!errorDisplayed) {
                return;
            }
            errorDisplayed = false;
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
            var inputs = document.getElementById('registrationForm').getElementsByTagName('input');

            // Itera su ogni elemento input
            for (var i = 0; i < inputs.length; i++) {
                // Rimuovi il bordo rosso dal campo
                inputs[i].style.border = '';
            }
        }
    </script>
</head>
<body>
<%@include file="/static/include/top-bar.jsp"%>
<div id="main-content">
    <main class="fullsize">
        <section class="title">
            <h1>Modulo di registrazione</h1>
        </section>
        <section class="content">
            <% String alreadyPresent;
                if ((alreadyPresent = request.getParameter("alreadyPresent")) != null) {
                    if (alreadyPresent.equals("true")) {
            %>
            <h2 class="registered" id="already-present" >Utente già registrato</h2>
            <script>
                var errorPresentElement = document.getElementById("already-present");
                errorPresentElement.scrollIntoView({behavior: "smooth"});
            </script>
            <%  }
            } %>
            <form id="registrationForm" name="registrationForm" method="post" action="signup" enctype="multipart/form-data">
                <fieldset>
                    <legend>Informazioni Personali</legend>
                    <label for="name">Nome:</label>
                    <input type="text" id="name" name="name" maxlength="20" required placeholder="Nome">

                    <label for="surname">Cognome:</label>
                    <input type="text" id="surname" name="surname" maxlength="20" required placeholder="Cognome">

                    <label for="cf">Codice fiscale:</label>
                    <input type="text" id="cf" name="cf" maxlength="16" minlength="16" required placeholder="Codice Fiscale">

                    <label for="dob">Data di nascita:</label>
                    <input type="date" id="dob" name="dob" required placeholder="Data di Nascita">

                    <label for="birthplace">Luogo di nascita (città):</label>
                    <input type="text" id="birthplace" name="birthplace" maxlength="20" required placeholder="Città di nascita">
                </fieldset>

                <fieldset>
                    <legend>Indirizzo</legend>
                    <label for="country">Stato:</label>
                    <input type="text" id="country" name="country" required>

                    <label for="province">Provincia:</label>
                    <input type="text" id="province" name="province" required>

                    <label for="city">Città:</label>
                    <input type="text" id="city" name="city" required>

                    <label for="street">Via:</label>
                    <input type="text" id="street" name="street" required>

                    <label for="houseNumber">Numero Civico:</label>
                    <input type="text" id="houseNumber" name="houseNumber" required>
                </fieldset>

                <fieldset>
                    <legend>Contatti</legend>
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" maxlength="50" required>

                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" maxlength="50" required>

                    <label for="phoneNumber">Numero di telefono:</label>
                    <input type="text" id="phoneNumber" name="phoneNumber" maxlength="10">
                </fieldset>

                <fieldset>
                    <legend>Altri Dettagli</legend>
                    <label for="photo">Foto:</label>
                    <input type="file" id="photo" name="photo">
                </fieldset>

                <input type="submit" name="confirm" value="Conferma">
            </form>
        </section>
    </main>
</div>
</body>
</html>