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
    <title>CircoloCulturale</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <script>
        function addMessages() {
            const urlParams = new URLSearchParams(window.location.search);
            const error = urlParams.get('error');
            if (error) {
                const content = document.querySelector('.content');
                const errorParagraph = document.createElement('p');
                errorParagraph.style.color = 'red';
                errorParagraph.textContent = "Errore durante il processo";
                content.insertBefore(errorParagraph, content.querySelector('h1'));
            }

            const success = urlParams.get('success');
            if (success) {
                const content = document.querySelector('.content');
                const successParagraph = document.createElement('p');
                successParagraph.style.color = 'green';
                successParagraph.textContent = "Azione avvenuta con successo";
                content.insertBefore(successParagraph, content.querySelector('h1'));
            }
        }

        window.addEventListener('DOMContentLoaded', addMessages);

    </script>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content">
        <main class="fullsize">
            <section class="title">
                <h1>Informazioni sul corso</h1>
            </section>
            <section class="content">
                <h1>${corso.genere} ${corso.categoria} ${corso.livello}</h1>
                ${corso.descrizione}
                <strong>Sede:</strong> <a href="/sede/info?id=${corso.idSala.idSede.id}">${corso.idSala.idSede.nome}</a>
                <strong>Sala: ${corso.idSala.numeroSala}</strong>
                <strong>Calendario:</strong>
                <ul>
                    <c:forEach items="${corso.calendarioCorso}" var="giornoSettimana">
                        <c:set var="giornoIt" value="${giornoSettimana.giornoSettimana == 'monday' ? 'Lunedì' : giornoSettimana.giornoSettimana == 'tuesday' ? 'Martedì' : giornoSettimana.giornoSettimana == 'wednesday' ? 'Mercoledì' : giornoSettimana.giornoSettimana == 'thursday' ? 'Giovedì' : 'Venerdì'}"/>
                        <li>${giornoIt} dalle ${giornoSettimana.orarioInizio} alle ${giornoSettimana.orarioFine}</li>
                    </c:forEach>
                </ul>
                <strong>Docenti:</strong>
                <ul>
                    <c:forEach items="${corso.docenti}" var="docente">
                        <li>${docente.socio.utente.nome} ${docente.socio.utente.cognome}</li>
                    </c:forEach>
                </ul>
                <% if (request.getParameter("isDocente") == null ) { %>
                    <% if (request.getAttribute("socioHeader") != null && request.getAttribute("isEnrolled") != null && !(Boolean)request.getAttribute("isEnrolled")) {
                        if (request.getAttribute("availability") != null && (Boolean)request.getAttribute("availability")) { %>
                            <form action="/corso/iscrizione" method="post">
                                <input type="hidden" name="socio-id" value="${socioHeader.id}">
                                <input type="hidden" name="corso-id" value="${corso.id}">
                                <input type="submit" value="Iscriviti">
                            </form>
                    <%  } else { %>
                            <p style="color:red">Posti non disponibili</p>
                            <button disabled>Iscriviti</button>
                    <% }} %>
                    <% if (request.getAttribute("socioHeader") != null && request.getAttribute("isEnrolled") != null && (Boolean)request.getAttribute("isEnrolled")) { %>
                    <form action="/corso/disiscrizione" method="post">
                        <input type="hidden" name="socio-id" value="${socioHeader.id}">
                        <input type="hidden" name="corso-id" value="${corso.id}">
                        <input type="submit" value="Disiscriviti">
                    </form>
                    <% } %>
                <% } %>
            </section>
        </main>
    </div>
</body>
</html>
