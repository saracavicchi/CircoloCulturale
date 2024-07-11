<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 02/07/2024
  Time: 20:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List"%>
<%@ page import="it.unife.cavicchidome.CircoloCulturale.models.CalendarioCorso"%>
<!DOCTYPE html>
<html>
<head>
    <title>Profilo Socio</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css"/>
    <script>
        function checkTime() {
            var orarioInizio = document.getElementById("orarioInizio").value;
            var orarioFine = document.getElementById("orarioFine").value;
            if (orarioInizio >= orarioFine) {
                alert("L'orario di fine deve essere successivo a quello di inizio");
                return false;
            }
            return true;
        }

        function checkDate() {
            var data = document.getElementById("data").value;
            var today = new Date();
            var selectedDate = new Date(data);
            if (selectedDate < today) {
                alert("La data selezionata è precedente a quella odierna");
                return false;
            }
            return true;
        }

        // controlla che gli orari selezionati non si sovrappongano ne alle prenotazioni ne ai corsi presenti
        function checkOverlap() {
            let orarioInizio = document.getElementById("orarioInizio").value;
            let orarioFine = document.getElementById("orarioFine").value;
            let orariInizioCorsi = document.getElementById("orariCorsi").querySelectorAll("td:nth-child(2)")
            let orariFineCorsi = document.getElementById("orariCorsi").querySelectorAll("td:nth-child(3)")
            let orariInizioPrenotazioni = document.getElementById("orariPrenotazioni").querySelectorAll("td:nth-child(2)")
            let orariFinePrenotazioni = document.getElementById("orariPrenotazioni").querySelectorAll("td:nth-child(3)")
            let orariApertura = document.getElementById("orariSede").querySelectorAll("td")

            for (let i = 0; i < orariInizioCorsi.length; i++) {
                if ((orarioInizio >= orariInizioCorsi[i].innerText && orarioInizio <= orariFineCorsi[i].innerText) ||
                    (orarioFine >= orariInizioCorsi[i].innerText && orarioFine <= orariFineCorsi[i].innerText)) {
                    alert("L'orario selezionato si sovrappone a un corso già presente");
                    return false;
                }
            }
            for (let i = 0; i < orariInizioPrenotazioni.length; i++) {
                if ((orarioInizio >= orariInizioPrenotazioni[i].innerText && orarioInizio <= orariFinePrenotazioni[i].innerText) ||
                    (orarioFine >= orariInizioPrenotazioni[i].innerText && orarioFine <= orariFinePrenotazioni[i].innerText)) {
                    alert("L'orario selezionato si sovrappone a una prenotazione già presente");
                    return false;
                }
            }
            if (orarioInizio < orariApertura[0].innerText || orarioFine > orariApertura[1].innerText) {
                alert("L'orario selezionato non è compreso negli orari di apertura della sede");
                return false;
            }
        }

        window.addEventListener("load", function() {
            document.getElementById("prenotazioneForm").addEventListener("submit", function(event) {
                if (!checkTime() || !checkDate() || !checkOverlap()) {
                    event.preventDefault();
                }
            });
        });
    </script>
</head>
<body>
<%@ include file="/static/include/header.jsp" %>
<div id="main-content">
    <main class="midleft">
        <section class="title">
            <h1>Nuova prenotazione sala</h1>
        </section>
        <section class="content">
            <form action="/socio/prenotazioni/nuova" method="get">
                <label for="sala">Selezionare una sala:</label>
                <select name="sala" id="sala" required>
                    <c:forEach items="${sedi}" var="sede">
                        <optgroup label="${sede.nome}">
                            <c:forEach items="${sede.sale}" var="sala">
                            <c:if test="${sala.prenotabile == true && sala.active == true}">
                                    <option value="${sala.id}">${sala.numeroSala}</option>
                            </c:if>
                            </c:forEach>
                        </optgroup>
                    </c:forEach>
                </select>
                <label for="data">Selezionare una data:</label>
                <input type="date" name="data" id="data" value="<%= java.time.LocalDate.now()%>" required>
                <input type="submit" value="Invio">
            </form>
            <% if (request.getAttribute("prenotabile") != null && request.getAttribute("prenotabile").equals(true)) {%>
            <% if (request.getAttribute("corsi") != null && !((List<?>) (request.getAttribute("corsi"))).isEmpty() &&
            request.getAttribute("prenotazioni") != null && !((List<?>)(request.getAttribute("prenotazioni"))).isEmpty()) { %>
            <p>Nella sala ${sala.numeroSala} in data ${date} sono presenti le seguenti prenotazioni:</p>
            <% if (!((List<?>) request.getAttribute("corsi")).isEmpty()) {%>
            <h3>Corsi</h3>
            <table id="orariCorsi">
                <thead>
                <tr>
                    <th>Nome</th>
                    <th>Orario di inizio</th>
                    <th>Orario di fine</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${corsi}" var="corso">
                    <tr>
                        <td>${corso.idCorso.categoria} ${corso.idCorso.genere} ${corso.idCorso.livello}</td>
                        <td>${corso.orarioInizio}</td>
                        <td>${corso.orarioFine}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <% } %>
            <% if (!((List<?>)request.getAttribute("prenotazioni")).isEmpty()) {%>
            <h3>Prenotazioni soci</h3>
            <table id="orariPrenotazioni">
                <thead>
                <tr>
                    <th>Socio</th>
                    <th>Orario di inizio</th>
                    <th>Orario di fine</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${prenotazioni}" var="prenotazione">
                    <tr>
                        <td>${prenotazione.idSocio.utente.nome} ${prenotazione.idSocio.utente.cognome}</td>
                        <td>${prenotazione.orarioInizio}</td>
                        <td>${prenotazione.orarioFine}</td>
                    </tr>
                </c:forEach>
                </tbody>
                </table>
            <% } %>
            <% } else { %>
            <p>Nella sala ${sala.numeroSala} in data ${date} non sono presenti prenotazioni</p>
            <%}%>
            <h3>Gli orari di apertura della sede scelta sono:</h3>
            <table id="orariSede">
                <thead>
                <tr>
                    <th>Apertura</th>
                    <th>Chiusura</th>
                </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>${orari.orarioApertura}</td>
                        <td>${orari.orarioChiusura}</td>
                    </tr>
                </tbody>
            </table>
            <form id="prenotazioneForm" name="prenotazioneForm" action="/socio/prenotazioni/nuova" method="post">
                <label for="descrizione">Descrizione</label>
                <input id="descrizione" name="descrizione" type="text">
                <label for="orarioInizio">Orario di inizio</label>
                <input id="orarioInizio" name="orarioInizio" type="time">
                <label for="orarioFine">Orario di fine</label>
                <input id="orarioFine" name="orarioFine" type="time">
                <input type="hidden" name="sala" value="${sala.id}">
                <input type="hidden" name="data" value="${date}">
                <input type="submit" value="Crea">
            </form>
            <%}%>
        </section>
    </main>
    <%@include file="/static/include/aside.jsp"%>
</div>

</body>
</html>
