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
    <title>Circolo La Sinfonia</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css">
    <style>
        section.content > article {
            height: 250px;
        }
    </style>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content" class="clearfix">
        <main class="midleft">
            <section class="title">
                <h1>Biglietti</h1>
            </section>
            <section class="filter">
                <form action="/segretario/biglietti" method="get">
                    <label for="saggio">Saggio</label>
                    <select name="saggioId" id="saggio">
                        <option label="---"></option>
                        <c:forEach items="${saggi}" var="saggio">
                            <option value="${saggio.id}" <c:if test="${param.saggioId == saggio.id}">selected</c:if> >${saggio.nome}</option>
                        </c:forEach>
                    </select>
                    <label for="nome">Nome:</label>
                    <input type="text" name="nome" id="nome" value="<c:if test="${param.nome ne null}">${param.nome}</c:if>">
                    <label for="cognome">Cognome:</label>
                    <input type="text" name="cognome" id="cognome" value="<c:if test="${param.cognome ne null}">${param.cognome}</c:if>">
                    <label for="deleted">Mostra biglietti cancellati</label>
                    <input type="checkbox" name="deleted" id="deleted" value="true" <c:if test="${param.deleted == true}">checked</c:if>>
                    <input type="submit" value="Filtra">
                </form>
            </section>
            <section class="content clearfix">
                <c:forEach items="${biglietti}" var="biglietto">
                    <article class="vertical <c:if test="${biglietto.deleted == true}">deleted</c:if>">
                        <h1><a href="/biglietto/modifica?bigliettoId=${biglietto.id}">#${biglietto.id}</a></h1>
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
    <%@include file="/static/include/footer.jsp"%>
</body>
</html>
