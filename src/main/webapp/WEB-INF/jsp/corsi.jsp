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
        const urlParams = new URLSearchParams(window.location.search);
        const authFailed = urlParams.get('authFailed');
        if(authFailed === 'true'){
            alert('Autenticazione fallita.');
        }

        const pending = urlParams.get('pending');
        if(pending === 'true'){
            alert('La tessera non è stata ancora confermata. Si prega di rivolgersi ad una delle nostre segreterie.');
        }
    </script>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content" class="clearfix">
        <main class="<%= request.getAttribute("socioHeader") == null ? "fullsize" : "midleft"%> clearfix">
            <section class="title">
                <h1>I nostri corsi</h1>
            </section>
            <section class="filter">
                <form action="/corso/info" method="get">
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
                            <option value="${genere}" <c:if test="${param.genere == genere}">selected</c:if>>${genere}</option>
                        </c:forEach>
                    </select>
                    <label for="livello">Livello</label>
                    <select name="livello" id="livello">
                        <option label="---"></option>
                        <c:forEach items="${livelli}" var="livello">
                            <option value="${livello}" <c:if test="${param.livello == livello}">selected</c:if> >${livello}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" value="Filtra">
                </form>
            </section>
            <section class="content clearfix">
            <c:if test="${corsi.size() < 1}"><p id="emptyset">Nessun corso da mostrare</p></c:if>
                <c:forEach items="${corsi}" var="corso">
                    <article class="full">
                        <h1><a href="/corso/info?id=${corso.id}">${corso.categoria} ${corso.genere} ${corso.livello}</a></h1>
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
