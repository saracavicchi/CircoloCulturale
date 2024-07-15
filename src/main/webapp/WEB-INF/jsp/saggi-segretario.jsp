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
    </style>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content">
        <main class="midleft">
            <section class="title">
                <h1>Tutti i saggi del circolo</h1>
            </section>
            <section class="filter">
                <form action="/saggi" method="get">
                    <label for="data">Filtra per data:</label>
                    <input type="date" name="data" id="data" value="<%= request.getParameter("data") != null ? request.getParameter("data") : java.time.LocalDate.now()%>>">
                    <label for="deleted">Mostra saggi cancellati</label>
                    <input type="checkbox" name="deleted" id="deleted" <c:if test="${param.deleted eq true}">checked</c:if> value="true">
                    <input type="submit" value="Filtra"/>
                </form>
            </section>
            <section class="content clearfix">
                <c:forEach items="${saggi}" var="saggio">
                    <article>
                        <h1><a href="/saggio/modifica?saggioId=${saggio.id}">${saggio.nome}</a></h1>
                        <h2>${saggio.descrizione}</h2>
                        <p>${saggio.data} - ${saggio.orarioInizio}
                        ${saggio.indirizzo}</p>
                    </article>
                </c:forEach>
            </section>
        </main>
        <%@include file="/static/include/aside.jsp"%>
    </div>
</body>
</html>
