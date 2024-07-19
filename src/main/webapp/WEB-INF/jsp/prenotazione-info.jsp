<%--
  Created by IntelliJ IDEA.
  User: lucadomeneghetti
  Date: 31/05/2024
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>Circolo La Sinfonia</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <script>
        function confirmDisiscrizione(event) {
            var confirmUnsubscribe = document.getElementById('confirmUnsubscribe');
            if (confirmUnsubscribe != null && !confirmUnsubscribe.checked) {
                alert('Per favore, conferma se sei sicuro di voler cancellare la prenotazione.');
                event.preventDefault(); // Prevent form submission
            }
        }

        function handleEliminaPrenotazione() {
            document.getElementById('deleteForm').addEventListener('submit', confirmDisiscrizione);
        }

        window.addEventListener('load', handleEliminaPrenotazione)
    </script>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content" class="clearfix">
        <main class="clearfix midleft">
            <section class="title">
                <h1>Informazioni sulla prenotazione</h1>
            </section>
            <section class="content">
                <c:if test="${prenotazione.deleted == true}"><p style="color: red">Prenotazione cancellata</p></c:if>
                <h1>Prenotazione #${prenotazione.id}</h1>
                <h2>${prenotazione.descrizione}</h2>
                <p><strong>Data: </strong>${prenotazione.data}</p>
                <p><strong>Orario: </strong>dalle ${prenotazione.orarioInizio} alle ${prenotazione.orarioFine}</p>
                <p><strong>Sala e sede: </strong>${prenotazione.idSala.numeroSala} - ${prenotazione.idSala.idSede.nome}</p>
                <% if (((it.unife.cavicchidome.CircoloCulturale.models.Socio)request.getAttribute("socioHeader")).getSegretario() != null) {%>
                <p><strong>Prenotato da: </strong>${prenotazione.idSocio.utente.nome} ${prenotazione.idSocio.utente.cognome}</p>
                <% } %>
                <c:if test="${prenotazione.deleted ne true}">
                <form name="deleteForm" id="deleteForm" action="/socio/prenotazioni/elimina" method="POST">
                    <input type="hidden" name="prenotazione-id" value="${prenotazione.id}"/>
                    <label for="confirmUnsubscribe">Confermi la cancellazione?</label>
                    <input type="checkbox" id="confirmUnsubscribe" name="confirmUnsubscribe" required>
                    <button type="submit">Conferma</button>
                </form>
                </c:if>
            </section>
        </main>
        <%@include file="/static/include/aside.jsp"%>
    </div>
    <%@include file="/static/include/footer.jsp"%>
</body>
</html>
