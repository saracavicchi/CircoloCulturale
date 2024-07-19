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
    <title>Circolo La Sinfonia</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content" class="clearfix">
        <main class="midleft">
            <section class="title">
                <h1>Informazioni sulla sede</h1>
            </section>
            <section class="content">
                <h1>${sede.nome}</h1>
                <p><strong>Indirizzo: </strong>${sede.indirizzo}<p>
                <p><strong>Ristoro: </strong><c:if test="${sede.ristoro eq true}">Si</c:if><c:if test="${sede.ristoro eq false}">No</c:if></p>
                <p><strong>Segretario amministratore:</strong>${sede.segretario.socio.utente.nome} ${sede.segretario.socio.utente.cognome}</p>
                <h2>Orari di apertura</h2>
                <ul>
                <c:forEach items="${sede.orarioSede}" var="orario" varStatus="status">
                <c:set var="giornoIt" value="${status.index == 0 ? 'Lunedì' : status.index == 1 ? 'Martedì' : status.index == 2 ? 'Mercoledì' : status.index == 3 ? 'Giovedì' : status.index == 4 ? 'Venerdì' : status.index == 5 ? 'Sabato' : 'Domenica'}"/>
                <li>
                    (<c:out value="${giornoIt}"/>): <c:out value="${orario.orarioApertura}"/> - <c:out value="${orario.orarioChiusura}"/>
                </li>
                </c:forEach>
                    </ul>
                    <h2>Giorni di chiusura straordinari</h2>
                <ul>
                <c:forEach items="${sede.giornoChiusura}" var="giorno">
                    <li><c:out value="${giorno}"/></li>
                    </c:forEach>
                </ul>
            </section>
        </main>
        <%@include file="/static/include/aside.jsp"%>
    </div>
    <%@include file="/static/include/footer.jsp"%>
</body>
</html>
