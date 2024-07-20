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
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var docentiOverlap = "${param.docentiOverlap}";
            if (docentiOverlap == 'true') {
                warningDocentiOverlap();
            }

        });

        function warningDocentiOverlap() {
            alert("Attenzione: le informazioni sono state salvate ma sono stati rilevati problemi di sovrapposizione oraria nell'orario dei docenti.");
        }

        function redirectToCreaCorsoPage() {
            window.location.href = '/corso/crea';
        }
    </script>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content" class="clearfix">
        <main class="midleft">
            <section class="title">
                <h1>Corsi del circolo</h1>
            </section>
            <section class="filter">
                <form action="/segretario/corsi" method="get">
                    <label for="categoria">Categoria</label>
                    <select name="categoria" id="categoria">
                        <option label="---"></option>
                        <c:forEach items="${categorie}" var="categoria">
                            <option value="${categoria}" <c:if test="${param.categoria == categoria}">selected</c:if> >${categoria}</option>
                        </c:forEach>
                    </select>
                    <label for="genere">Genere</label>
                    <select name="genere" id="genere">
                        <option label="---"></option>
                        <c:forEach items="${generi}" var="genere">
                            <option value="${genere}" <c:if test="${param.genere == genere}">selected</c:if> >${genere}</option>
                        </c:forEach>
                    </select>
                    <label for="livello">Livello</label>
                    <select name="livello" id="livello">
                        <option label="---"></option>
                        <c:forEach items="${livelli}" var="livello">
                            <option value="${livello}" <c:if test="${param.livello == livello}">selected</c:if>>${livello}</option>
                        </c:forEach>
                    </select>
                    <label for="active">Mostra corsi cancellati</label>
                    <input type="checkbox" name="active" id="active" value="true" <c:if test="${param.active == true}">checked</c:if>>
                    <input type="submit" value="Filtra">
                </form>
                <section class="content">
                    <button type="button" onclick="redirectToCreaCorsoPage()">Crea Nuovo Corso</button>
                </section>
            </section>
            <section class="content clearfix">
            <c:if test="${corsi.size() < 1}"><p id="emptyset">Nessun corso da mostrare</p></c:if>
                <c:forEach items="${corsi}" var="corso">
                    <article class="full <c:if test="${corso.active == false}">deleted</c:if>">
                        <h1><a href="/corso/modificaBase?idCorso=${corso.id}">${corso.categoria} ${corso.genere} ${corso.livello}</a></h1>
                        <h2>${corso.descrizione}</h2>
                        <p><strong>Luogo:</strong> aula ${corso.idSala.numeroSala} ${corso.idSala.idSede.nome}</p>
                    </article>
                </c:forEach>
            </section>
        </main>
        <%@include file="/static/include/aside.jsp"%>
    </div>
    <%@include file="/static/include/footer.jsp"%>
</body>
</html>
