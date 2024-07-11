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
    <div id="main-content">
        <main class="clearfix midleft">
            <section class="title">
                <h1>Informazioni sulla prenotazione</h1>
            </section>
            <section class="content">
                <h1>Prenotazione #${prenotazione.id}</h1>
                <h2>${prenotazione.descrizione}</h2>
                <p>Data: ${prenotazione.data}</p>
                <p>Orario: dalle ${prenotazione.orarioInizio} alle ${prenotazione.orarioFine}</p>
                <p>Sala ${prenotazione.idSala.numeroSala} - ${prenotazione.idSala.idSede.nome}</p>
                <% if (((it.unife.cavicchidome.CircoloCulturale.models.Socio)request.getAttribute("socioHeader")).getSegretario() != null) {%>
                <p>Prenotato da: ${prenotazione.idSocio.utente.nome} ${prenotazione.idSocio.utente.cognome}</p>
                <form name="deleteForm" id="deleteForm" action="elimina" method="POST">
                    <input type="hidden" name="prenotazione-id" value="${prenotazione.id}"/>
                    <label for="not-deleted">Attiva</label><input type="radio" id="not-deleted" name="delete" value="false" <c:if test="${prenotazione.deleted == false}">checked</c:if>>
                    <label for="deleted">Inattiva</label><input type="radio" id="deleted" name="delete" value="true" <c:if test="${prenotazione.deleted == true}">checked</c:if>>
                    <button type="submit">Conferma</button>
                </form>
                <% } %>
            </section>
        </main>
        <%@include file="/static/include/aside.jsp"%>
    </div>

</body>
</html>
