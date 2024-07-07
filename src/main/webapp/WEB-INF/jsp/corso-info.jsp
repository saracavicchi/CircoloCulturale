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
        function corsoEnrollButtonAction () {
            let enrollButton = document.getElementById("corsoEnrollButton")
            enrollButton.addEventListener("click", function () {
                window.location.href = ("/corso/iscrizione?id=" + enrollButton.value)
            })
        }

        window.addEventListener("load", corsoEnrollButtonAction)
    </script>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content">
        <main class="fullsize">
            <section class="title">
                <h1>Informazioni sulla sede</h1>
            </section>
            <section class="content">
                <h1>${corso.genere} ${corso.categoria} ${corso.livello}</h1>
                ${corso.descrizione}
                <strong>Sede:</strong> <a href="/sede/info?id=${corso.idSala.idSede.id}">${corso.idSala.idSede.nome}</a>
                <strong>Sala: ${corso.idSala.numeroSala}</strong>
                <strong>Calendario:</strong>
                <ul>
                    <c:forEach items="${corso.calendarioCorso}" var="giornoSettimana">
                        <li>${giornoSettimana.giornoSettimana} dalle ${giornoSettimana.orarioInizio} alle ${giornoSettimana.orarioFine}</li>
                    </c:forEach>
                </ul>
                <strong>Docenti:</strong>
                <ul>
                    <c:forEach items="${corso.docenti}" var="docente">
                        <li>${docente.socio.utente.nome} ${docente.socio.utente.cognome}</li>
                    </c:forEach>
                </ul>
                <% if (request.getAttribute("socio") != null) {
                    if (request.getAttribute("availability") != null && (Boolean)request.getAttribute("availability")) { %>
                        <button id="corsoEnrollButton" value="${corso.id}">Iscriviti</button>
                <%  } else { %>
                        <p style="color:red">Posti non disponibili</p>
                        <button disabled>Iscriviti</button>
                <% }} %>
            </section>
        </main>
    </div>
</body>
</html>
