<%--
  Created by IntelliJ IDEA.
  User: lucadomeneghetti
  Date: 02/07/2024
  Time: 20:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>CircoloCulturale</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
    <%@include file="/static/include/top-bar.jsp"%>
    <div id="main-content">
        <main class="fullsize">
            <section class="title">
                <h1>Modulo di iscrizione al saggio</h1>
            </section>
            <section class="content">
                <form action="/saggio/iscrizione" method="POST">
                    <h1>${saggio.nome}</h1>
                    <h2>${saggio.descrizione}</h2>
                    <p>${saggio.data} dalle ${saggio.orarioInizio} alle ${saggio.orarioFine}</p>
                    <fieldset>
                        <legend>Intestatario biglietto:</legend>
                        <input type="radio" id="self" name="ticketUser" value="self" checked>
                        <label for="self">Personale</label>
                        <input type="radio" id="other" name="ticketUser" value="other">
                        <label for="other">Altro utente</label>
                    </fieldset>
                </form>
            </section>
        </main>
    </div>
</body>
</html>
