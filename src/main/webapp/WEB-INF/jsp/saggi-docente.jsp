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
    <title>CircoloCulturale</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content" class="clearfix">
        <main class="midleft">
            <section class="title">
                <h1>I saggi dei tuoi corsi</h1>
            </section>
            <section class="filter">
                <form action="/docente/saggi" method="get">
                    <label for="data">Filtra per data:</label>
                    <input type="date" name="data" id="data" value="<%= request.getParameter("data") != null ? request.getParameter("data") : java.time.LocalDate.now()%>>">
                    <input type="submit" value="Filtra"/>
                </form>
            </section>
            <section class="content clearfix">
                <c:if test="${saggi.size() < 1}"><p id="emptyset">Nessun saggio da mostrare</p></c:if>
                <c:forEach items="${saggi}" var="saggio">
                    <article class="full">
                        <h1><a href="/saggio/info?saggioId=${saggio.id}">${saggio.nome}</a></h1>
                        <h2>${saggio.descrizione}</h2>
                        <p>${saggio.data} - ${saggio.orarioInizio}
                        ${saggio.indirizzo}</p>
                    </article>
                </c:forEach>
            </section>
        </main>
        <%@include file="/static/include/aside.jsp"%>
    </div>
    <%@include file="/static/include/footer.jsp"%>
</body>
</html>
