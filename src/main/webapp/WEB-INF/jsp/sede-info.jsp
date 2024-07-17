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
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content" class="clearfix">
        <main class="fullsize">
            <section class="title">
                <h1>Informazioni sulla sede</h1>
            </section>
            <section class="content">
                <h1>${sede.nome}</h1>
                ${sede.indirizzo}
                ${sede.segretario.socio.utente.nome} ${sede.segretario.socio.utente.cognome}
            </section>
        </main>
    </div>
    <%@include file="/static/include/footer.jsp"%>
</body>
</html>
