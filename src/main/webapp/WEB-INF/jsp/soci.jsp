<%--
  Created by IntelliJ IDEA.
  User: lucadomeneghetti
  Date: 07/07/2024
  Time: 08:22
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
        // when submitting the form with checkbox deleted on,
    </script>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content">
        <main class="clearfix midleft">
            <section class="title">
                <h1>Soci del circolo</h1>
                <p style="float: right"><a href="/segretario/nuovoSocio">Nuovo socio</a></p>
            </section>
            <section class="filter">
                <form id="filterForm" name="filterForm" action="/segretario/soci" method="get">
                    <ul style="display: flex; list-style-type: none">
                        <c:forEach items="${initials}" var="initial">
                            <li>
                                <a href="/segretario/soci?initial=${initial}">${initial}</a>
                            </li>
                        </c:forEach>
                    </ul>
                    <input type="hidden" name="initial" value="<%= request.getParameter("initial")%>">
                    <label for="deleted">Mostra soci cancellati</label>
                    <input type="checkbox" id="deleted" name="deleted">
                    <input type="submit" value="Filtra">
                </form>
            </section>
            <section class="content clearfix">
                <c:forEach items="${soci}" var="socio">
                    <article>
                        <h1><a href="/socio/profile?socio-id=${socio.id}">${socio.utente.cognome} ${socio.utente.nome}</a></h1>
                    </article>
                </c:forEach>
            </section>
        </main>
        <%@include file="/static/include/aside.jsp"%>
    </div>
</body>
</html>
