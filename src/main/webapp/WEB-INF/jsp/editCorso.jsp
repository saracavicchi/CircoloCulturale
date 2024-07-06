<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 06/07/2024
  Time: 13:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title>Modifica Corso</title>
</head>
<body>
<h2>Modifica Corso</h2>
<form id="editCorso" action="editCorso" method="post">
    <input type="hidden" name="idCorso" value="${corso.id}"/>

    <label>Descrizione:</label>
    <input type="text" name="descrizione" value="${corso.descrizione}"/>

    <label>Genere:</label>
    <input type="text" name="genere" value="${corso.genere}"/>

    <label>Livello:</label>
    <input type="text" name="livello" value="${corso.livello}"/>

    <label>Categoria:</label>
    <<input type="radio" id="danza" name="categoria" value="danza" <c:if test="${corso.categoria == 'danza'}">checked</c:if> required>
    <label for="danza">Danza</label><br>
    <input type="radio" id="musica" name="categoria" value="musica" <c:if test="${corso.categoria == 'danza'}">checked</c:if> required>
    <label for="musica">Musica</label><br>

    <label>Sala:</label>
    <select name="idSala" required>
        <c:forEach items="${sale}" var="sala">
            <option value="${sala.id}" ${sala.id.toString() == corso.idSala.toString() ? 'selected' : ''}>${sala.numeroSala}</option>>${sala.numeroSala}</option>
        </c:forEach>
    </select>
    <div>
        <label>Docenti:</label>
        <c:forEach items="${docenti}" var="docente">
            <input type="checkbox" name="docenti" value="${docente.id}" ${corso.docenti.contains(docente) ? 'checked' : ''}>${docente.nome} ${docente.cognome}<br/>
        </c:forEach>
    </div>
    <!-- Selezione per soci non segretari -->
    <div>
        <label>Soci:</label>
        <select multiple name="sociNonSegretari">
            <c:forEach items="${sociInfo}" var="socio">
                <option value="${socio[0]}">${socio[1]} ${socio[2]}</option> <!-- Assuming socio[0] = ID, socio[1] = Nome, socio[2] = Cognome -->
            </c:forEach>
        </select>
    </div>

    <!-- Calendario di svolgimento -->
    <div>
        <label>Giorni di svolgimento:</label>
        <c:forEach items="${giorniSettimana}" var="giorno">
            <input type="checkbox" name="giorni" value="${giorno}"/>Giorno ${giorno}<br/>
        </c:forEach>
    </div>
    <div>
        <label>Orario inizio:</label>
        <input type="time" name="orarioInizio"/>
    </div>
    <div>
        <label>Orario fine:</label>
        <input type="time" name="orarioFine"/>
    </div>
    <input type="submit" value="Aggiorna"/>
</form>
</body>
</html>
