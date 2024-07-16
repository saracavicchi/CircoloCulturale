<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 16/07/2024
  Time: 09:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Circolo Culturale</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<%@include file="/static/include/header.jsp"%>
<div id="main-content">
    <main class="midleft">
        <section class="title">
            <h1>Biglietti</h1>
        </section>
        <section class="filter">
            <form action="/socio/biglietti" method="get">
                <label for="saggio">Saggio</label>
                <select name="saggioId" id="saggio">
                    <option label="---"></option>
                    <c:forEach items="${saggi}" var="saggio">
                        <option value="${saggio.id}" <c:if test="${param.saggioId == saggio.id}">selected</c:if> >${saggio.nome}</option>
                    </c:forEach>
                </select>
                <input type="submit" value="Filtra">
            </form>
        </section>
        <section class="content clearfix">
            <c:forEach items="${biglietti}" var="biglietto">
                <article>
                    <h1><a href="/biglietto/info?id=${biglietto.id}">#${biglietto.id}</a></h1>
                    <h2>${biglietto.idUtente.cf}</h2>
                    <p>${biglietto.idSaggio.nome}</p>
                    <p>${biglietto.dataOraAcquisto}</p>
                    <p>Quantità: ${biglietto.quantita}</p>
                    <p>Stato pagamento: ${biglietto.statoPagamento}</p>
                </article>
            </c:forEach>
        </section>
    </main>
    <%@include file="/static/include/aside.jsp"%>
</div>
</body>
</html>
