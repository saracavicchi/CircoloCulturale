<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 13/07/2024
  Time: 12:13
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
        function redirectToEditSalePage() {
            var idSede = '${sede.id}';
            window.location.href = '/sede/sala/modifica?idSede=' + idSede;
        }
        function redirectToAddSalaPage() {
            var idSede = '${sede.id}';
            window.location.href = '/sede/sala/crea?idSede=' + idSede;
        }
        var errorDisplayed = false;
        document.addEventListener('DOMContentLoaded', function() {
            initializeAddressFields();
            initModificaSedeForm();
            addGiorniChiusura();
            gestisciStipendioSegretario();
            toggleFormElements(false); // Disabilita gli elementi del form all'avvio

            document.getElementById('enableEdit').addEventListener('change', function() {
                toggleFormElements(this.checked);
            });
        });

        function toggleFormElements(isEnabled) {
            var formElements = document.getElementById('modificaSedeForm').elements;
            for (var i = 0; i < formElements.length; i++) {
                if (formElements[i].id !== 'enableEdit') { // Evita di disabilitare il checkbox stesso
                    formElements[i].disabled = !isEnabled;
                }
            }
        }

        function addGiorniChiusura() {
            var maxChiusure = 10;
            var chiusureCount = 0; // Start with 0 closure dates

            document.getElementById('aggiungiChiusura').addEventListener('click', function () {
                var allFilled = true;
                var uniqueDate = true;
                var today = new Date().toISOString().split('T')[0]; // Ottieni la data attuale in formato yyyy-MM-dd

                var enteredDates = [];
                var chiusureCount = document.querySelectorAll('#chiusureContainer input[type="date"]').length;

                // Raccogli i valori dei giorni di chiusura già presenti
                document.querySelectorAll('#chiusureAttualiContainer input[type="checkbox"]').forEach(function(checkbox) {
                    enteredDates.push(checkbox.value);
                });

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

                if (!allFilled && chiusureCount > 0) {
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

        function initModificaSedeForm() {
            var modificaSedeForm = document.getElementById('modificaSedeForm');
            if (modificaSedeForm) {
                modificaSedeForm.addEventListener('submit', submitForm);
                var inputs = modificaSedeForm.getElementsByTagName('input');
                addFocusListenersToInputs(inputs, 'modificaSedeForm');
            }
        }
        function addFocusListenersToInputs(inputs, formName) {
            for (var i = 0; i < inputs.length; i++) {
                inputs[i].addEventListener('focus', function() {
                    removeError(formName);
                });
            }
        }

        function divideIndirizzo(sede) {
            const parti = sede.indirizzo.split(', ');
            const risultato = {
                state: parti[0],
                province: parti[1],
                city: parti[2],
                street: parti[3],
                houseNumber: parti[4]
            };
            return risultato;
        }

        function initializeAddressFields() {
            var sede = {
                indirizzo: "${sede.indirizzo}"
            };
            var risultato = divideIndirizzo(sede);
            document.getElementById('stato').textContent = risultato.state;
            document.getElementById('provincia').textContent = risultato.province;
            document.getElementById('citta').textContent = risultato.city;
            document.getElementById('via').textContent = risultato.street;
            document.getElementById('numeroCivico').textContent = risultato.houseNumber;
        }

        //campo/i che ha generato l'errore
        var erroredField = "";
        var errorMsg = "";

        function validateForm() {
            var charSpaceDashRegex = /^[A-Za-z\s\-]+$/;
            var maxLengthNome = 30;

            var nome = document.getElementById('nome').value;

            // Validazione nome
            if (!nome.match(charSpaceDashRegex) || nome.length > maxLengthNome) {
                erroredField = "nome";
                errorMsg = "Nome deve contenere solo caratteri, spazi, trattini e deve essere di massimo 30 caratteri.";
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
                document.getElementById('modificaSedeForm').submit();
            }
        }

        function displayErrorMessages() {
            var formElement = document.getElementById('modificaSedeForm');
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
        <h1>Modifica Sede ${sede.nome}</h1>
        </section>
    <section class="content">
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
        <p id="fail">Errore durante la modifica della sede, verificare le informazioni e riprovare</p>
        <script>
            var errorPresentElement = document.getElementById("fail");
            errorPresentElement.scrollIntoView({behavior: "smooth"});
        </script>
        <%} %>
        <label for="enableEdit">Modifica abilitata:</label>
        <input type="checkbox" id="enableEdit" name="enableEdit">
        <form id="modificaSedeForm" name="modificaSedeForm" action="/sede/modifica" method="post">
            <input type="hidden" name="idSede" value="${sede.id}"/>

            <fieldset>
                <legend>Informazioni Generali</legend>
                <label for="nome">Nome:</label>
                <input type="text" id="nome" name="nome" required maxlength="30" value="${sede.nome}" placeholder="Nome sede">
                <label for="stato">Stato:</label>
                <span id="stato"></span>
                <label for="provincia">Provincia:</label>
                <span id="provincia"></span>
                <label for="citta">Città:</label>
                <span id="citta"></span>
                <label for="via">Via:</label>
                <span id="via"></span>
                <label for="numeroCivico">Numero Civico:</label>
                <span id="numeroCivico"></span>
            </fieldset>

            <fieldset>
                <legend>Servizi</legend>
                <label for="areaRistoro">Area Ristoro:</label>
                <input type="checkbox" id="areaRistoro" name="areaRistoro" value="${sede.ristoro}">
            </fieldset>

            <fieldset>
                <legend>Orari di Apertura e Chiusura</legend>
                <c:forEach items="${sede.orarioSede}" var="orario" varStatus="status">
                <c:set var="giornoIt" value="${status.index == 0 ? 'Lunedì' : status.index == 1 ? 'Martedì' : status.index == 2 ? 'Mercoledì' : status.index == 3 ? 'Giovedì' : status.index == 4 ? 'Venerdì' : status.index == 5 ? 'Sabato' : 'Domenica'}"/>
                <p>
                    Orario di apertura (<c:out value="${giornoIt}"/>): <c:out value="${orario.orarioApertura}"/>
                    <br>
                    Orario di chiusura (<c:out value="${giornoIt}"/>): <c:out value="${orario.orarioChiusura}"/>
                </p>
                </c:forEach>
            </fieldset>

            <fieldset>
                <legend>Giorni di Chiusura</legend>
                <div id="chiusureAttualiContainer">
                    <label>Seleziona giorni di chiusura da eliminare:</label>
                    <c:forEach items="${sede.giornoChiusura}" var="giorno" varStatus="status">
                        <label for="chiusura${status.index}">${giorno}</label>
                        <input type="checkbox" id="chiusura${status.index}" name="deletedChiusura" value="${giorno}">
                    </c:forEach>
                </div>
                <div id="chiusureContainer"></div>
                <button type="button" id="aggiungiChiusura">Aggiungi un giorno di chiusura</button>
            </fieldset>

            <fieldset>
                <legend>Segretario e Amministrazione</legend>
                <c:if test="${not empty sede.segretario}">
                    <p>Segretario: <c:out value="${sede.segretario.socio.utente.nome}"/> <c:out value="${sede.segretario.socio.utente.cognome}"/></p>
                    <p>Admin:
                        <input type="checkbox" name="isAdmin" ${sede.segretario.admin ? 'checked' : ''} />
                    </p>
                </c:if>
                Seleziona nuovo segretario: <select name="segretari" onchange="gestisciStipendioSegretario()">
                <option value=0>Nessun segretario selezionato</option>
                <c:forEach items="${sociInfo}" var="socioS">
                    <option value="${socioS[3]}">${socioS[1]} ${socioS[2]} (${socioS[0]})</option>
                </c:forEach>
            </select>
                <div id="AdminContainer"></div>
            </fieldset>

            <button type="submit">Modifica Sede</button>
        </form>
        </section>
        <section class="content">
            <button type="button" onclick="redirectToEditSalePage()">Modifica Sale</button>
            <button type="button" onclick="redirectToAddSalaPage()">Aggiungi Sala</button>
        </section>

        <section class="content">
            <p>Cancellazione Sede</p>
            <form id="cancellaSedeForm" action="/sede/elimina" method="POST">
                <input type="hidden" name="idSede" value="${sede.id}" />
                <label for="confirmDeletion">Sei sicuro?</label>
                <input type="checkbox" id="confirmDeletion" name="confirmDeletion" required>
                <button type="submit">Cancella Sede</button>
            </form>
        </section>
    </main>
    <%@include file="/static/include/aside.jsp"%>
</div>
</body>
</html>
