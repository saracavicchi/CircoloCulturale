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
        function saggioEnrollButtonAction () {
            let enrollButton = document.getElementById("saggioEnrollButton")
            enrollButton.addEventListener("click", function () {
                window.location.href = ("/saggio/iscrizione?id=" + enrollButton.value)
            })
        }

        window.addEventListener("load", saggioEnrollButtonAction)
    </script>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content" class="clearfix">
        <main class="midleft">
            <section class="title">
                <h1>Informazioni sul saggio</h1>
            </section>
            <section class="content">
                <h1>${saggio.nome}</h1>
                <img class="profile-image" src="${empty saggio.urlFoto ? uploadDir.concat(placeholderImage) : uploadDir.concat(saggio.urlFoto)}" alt="Foto saggio"/>
                <h2>${saggio.data} dalle ${saggio.orarioInizio} alle ${saggio.orarioFine}</h2>
                <h3>Saggio dei corsi di:</h3>
                <ul>
                    <c:forEach items="${saggio.corsi}" var="corso">
                        <li><a href="/corso/info?id=${corso.id}">${corso.categoria} ${corso.genere} ${corso.livello}</a></li>
                    </c:forEach>
                </ul>
                <% if ((Integer)request.getAttribute("availableTickets") > 0) { %>
                <p style="color: green">Posti disponibili (${availableTickets})</p>
                <button id="saggioEnrollButton" value="${saggio.id}">Iscriviti</button>
                <% } else { %>
                <p style="color:red">Posti non disponibili</p>
                <button disabled>Iscriviti</button>
                <% } %>
            </section>
        </main>
        <%@include file="/static/include/aside.jsp"%>
    </div>
    <%@include file="/static/include/footer.jsp"%>
</body>
</html>
