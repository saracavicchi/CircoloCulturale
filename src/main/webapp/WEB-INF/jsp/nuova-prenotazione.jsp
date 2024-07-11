<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 02/07/2024
  Time: 20:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Profilo Socio</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css"/>
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
            </form>
            <% if (request.getAttribute("prenotabile") != null && request.getAttribute("prenotabile").equals(true)) {%>
            <p>Nella sala ${sala.numeroSala} in data ${date} sono presenti le seguenti prenotazioni:</p>
            <h3>Corsi</h3>

        </section>
    </main>
    <%@include file="/static/include/aside.jsp"%>
</div>

</body>
</html>
