<%--
  Created by IntelliJ IDEA.
  User: lucadomeneghetti
  Date: 02/07/2024
  Time: 10:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>Circolo La Sinfonia</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <style>
        section.content article {
            float: left;
            width: 250px;
            border-width: 1px;
            border-style: solid;
            border-radius: 10px;
            border-color: #a3271f;
            padding: 10px 8px 10px 20px;
            margin: 0 18px 16px 0;
            background: linear-gradient(to right,#fdfbfb,#ebedee);
        }
        /*.filter{
            margin-top: 53px;
        }

         */
    </style>
    <script>
        function redirectToCreaPrenotazionePage() {
            window.location.href = '/socio/prenotazioni/nuova';
        }
    </script>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content" class="clearfix">
        <main class="clearfix midleft">
            <section class="title">
                <h1>Prenotazioni</h1>
            </section>
            <section class="filter">
                <form action="<%= (request.getAttribute("segretario") != null) ? "/segretario/prenotazioni" : "/socio/prenotazioni"%>" method="get">
                    <label for="data">Data:</label>
                    <input type="date" id="data" name="data" value="<%= (request.getParameter("data") != null ? request.getParameter("data") : java.time.LocalDate.now())%>" required>
                    <% if (((Socio)request.getAttribute("socioHeader")).getSegretario() != null && request.getAttribute("segretario") != null) { %>
                    <label for="sala">Sala:</label>
                    <select name="sala" id="sala">
                    <option value="">Tutte</option>
                    <c:forEach items="${sedi}" var="sede">
                        <optgroup label="${sede.nome}">
                            <c:forEach items="${sale}" var="sala">
                            <c:if test="${sala.idSede.id == sede.id}">
                                    <option value="${sala.id}" <c:if test="${param.sala eq sala.id}">selected</c:if>>${sala.numeroSala}</option>
                            </c:if>
                            </c:forEach>
                        </optgroup>
                    </c:forEach>
                    </select>
                    <label for="deleted">Mostra prenotazioni cancellate:</label>
                    <input type="checkbox" id="deleted" name="deleted" <c:if test="${param.deleted eq 'on'}">checked</c:if>>
                    <%}%>
                    <input type="submit" value="Filtra">
                </form>

            </section>
            <section class="content">
                <button type="button" onclick="redirectToCreaPrenotazionePage()">Crea Nuova Prenotazione</button>
            </section>
            <section class="content clearfix">
                <c:forEach items="${prenotazioni}" var="prenotazione">
                    <article <c:if test="${prenotazione.deleted == true}">class="deleted"</c:if>>
                        <h1><a href="<%= (request.getAttribute("segretario") != null) ? "/segretario/prenotazioni" : "/socio/prenotazioni"%>?id=${prenotazione.id}">#${prenotazione.id}</a></h1>
                        <h2>${prenotazione.descrizione}</h2>
                        <p>${prenotazione.data} - dalle ${prenotazione.orarioInizio} alle ${prenotazione.orarioFine}</p>
                    </article>
                </c:forEach>
            </section>
        </main>
        <%@include file="/static/include/aside.jsp"%>
    </div>
    <%@include file="/static/include/footer.jsp"%>
</body>
</html>
