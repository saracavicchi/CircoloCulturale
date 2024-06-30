<%--
  Creato da IntelliJ IDEA.
  Utente: saracavicchi
  Data: 22/06/2024
  Ora: 12:18
  Per modificare questo modello usa File | Impostazioni | Modelli di file.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Pagina di Registrazione</title>
    <script>
        function validateForm() {
            var form = document.registrationForm;
            var cf = form.cf.value;
            var name = form.name.value;
            var surname = form.surname.value;
            var dob = form.dob.value;
            var birthplace = form.birthplace.value;
            var state = form.state.value;
            var province = form.province.value;
            var city = form.city.value;
            var street = form.street.value;
            var houseNumber = form.houseNumber.value;
            var email = form.email.value;
            var password = form.password.value;
            var phoneNumber = form.phoneNumber.value;
            var photoUrl = form.photoUrl.value;

            // Controlla se il nome, cognome, luogo di nascita, stato, provincia, città, via contengono solo caratteri e non numeri
            var regex = /^[A-Za-z\s]+$/;
            if (!regex.test(name) || !regex.test(surname) || !regex.test(birthplace) || !regex.test(state) || !regex.test(province) || !regex.test(city) || !regex.test(street)) {
                alert("I campi nome, cognome, luogo di nascita, stato, provincia, città, via devono contenere solo caratteri e non numeri.");
                return false;
            }

            // Controlla se la data di nascita è una data o del giorno corrente o antecedente
            var today = new Date();
            today.setHours(0, 0, 0, 0); // Imposta l'ora a mezzanotte cosi sia today che ora corrente sono uguali
            var inputDate = new Date(dob);
            if (inputDate > today) {
                alert("La data di nascita deve essere odierna o antecedente.");
                return false;
            }

            // Controlla se la somma dei caratteri di stato, provincia, città, via e numero civico non supera gli 80 caratteri
            if ((state.length + province.length + city.length + street.length + houseNumber.length) > 80) {
                alert("La somma dei caratteri di stato, provincia, città, via e numero civico non deve superare gli 80 caratteri.");
                return false;
            }

            // Controlla se il codice fiscale è composto sia da numeri che da lettere
            var cfRegex = /^[0-9a-zA-Z]+$/;
            if (!cfRegex.test(cf)) {
                alert("Il codice fiscale deve essere composto sia da numeri che da lettere.");
                return false;
            }

            // Controlla se l'email è un'email valida
            var emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (!emailRegex.test(email)) {
                alert("Inserisci un'email valida.");
                return false;
            }

            // Controlla se l'URL della foto è un URL valido
            var urlRegex = /^(ftp|http|https):\/\/[^ "]+$/;
            if (photoUrl && !urlRegex.test(photoUrl)) {
                alert("Inserisci un URL valido per la foto.");
                return false;
            }

            // Controlla se il numero di telefono contiene solo numeri
            var phoneRegex = /^[0-9]{10}$/;
            if (phoneNumber && !phoneRegex.test(phoneNumber)) {
                alert("Il numero di telefono deve contenere solo 10 numeri.");
                return false;
            }

            // Controlla se la password ha almeno 8 caratteri, almeno una lettera maiuscola, una lettera minuscola e un numero
            var passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
            if (!passwordRegex.test(password)) {
                alert("La password deve avere almeno 8 caratteri, almeno una lettera maiuscola, una lettera minuscola e un numero.");
                return false;
            }

            // Se tutti i controlli passano, restituisce true per permettere l'invio del form
            return true;
        }

        function submitForm() {
            // Chiama la funzione validateForm
            var isValid = validateForm();

            // Se la validazione ha esito positivo, formatta la data e invia il form
            if (isValid) {
                // Ottieni l'input della data di nascita
                /*var dobInput = document.getElementById('dob');

                // Crea un nuovo oggetto Date con il valore dell'input
                var dob = new Date(dobInput.value);

                // Formatta la data nel formato "yyyy-MM-dd"
                var year = dob.getFullYear();
                var month = ("0" + (dob.getMonth() + 1)).slice(-2); // Aggiunge uno zero davanti se il mese è un singolo numero
                var day = ("0" + dob.getDate()).slice(-2); // Aggiunge uno zero davanti se il giorno è un singolo numero
                var formattedDob = year + "-" + month + "-" + day;



                // Imposta il valore dell'input con la data formattata
                dobInput.value = formattedDob;
                */
                // Invia il form
                document.registrationForm.submit();
            } else {
                // Altrimenti, mostra un messaggio di errore
                alert("Si prega di correggere gli errori nel form prima di inviarlo.");
                return false;
            }
        }
    </script>
</head>
<body>
<h1>Si prega di inserire le suguenti informazioni personali</h1>
<form name="registrationForm" method="post" action="signup" onsubmit="return submitForm()">
    <label for="name">Nome:</label>
    <input type="text" id="name" name="name" maxlength="20" required>

    <label for="surname">Cognome:</label>
    <input type="text" id="surname" name="surname" maxlength="20" required>

    <label for="cf">Codice fiscale:</label>
    <input type="text" id="cf" name="cf" maxlength="16" minlength="16" required>

    <label for="dob">Data di nascita:</label>
    <input type="date" id="dob" name="dob" required>

    <label for="birthplace">Luogo di nascita (città):</label>
    <input type="text" id="birthplace" name="birthplace" maxlength="20" required>

    <label for="state">Stato:</label>
    <input type="text" id="state" name="state" required>

    <label for="province">Provincia:</label>
    <input type="text" id="province" name="province" required>

    <label for="city">Città:</label>
    <input type="text" id="city" name="city" required>

    <label for="street">Via:</label>
    <input type="text" id="street" name="street" required>

    <label for="houseNumber">Numero Civico:</label>
    <input type="text" id="houseNumber" name="houseNumber" required>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" maxlength="50" required>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" maxlength="50" required>

    <label for="phoneNumber">Numero di telefono:</label>
    <input type="text" id="phoneNumber" name="phoneNumber" maxlength="10">

    <label for="photoUrl">URL Foto:</label>
    <input type="text" id="photoUrl" name="photoUrl" maxlength="80">

    <input type="submit" name="confirm" value="Conferma">
    <% String alreadyPresent;
        if ((alreadyPresent = request.getParameter("alreadyPresent")) != null) {
            if (alreadyPresent.equals("true")) {%>
    <h2 style="color:red">Utente già registrato</h2>
    <%  }
    } %>
</form>
</body>
</html>