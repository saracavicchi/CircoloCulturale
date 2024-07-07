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
    <title>Circolo Culturale</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content">
        <main class="full">
            <section class="title">
                <h1>I nostri corsi</h1>
            </section>
            <section class="filter">
                <form action="/corso/info" method="get">
                    <label for="genere">Genere</label>
                    <select name="genere" id="genere">
                        <option label="---"></option>
                        <c:forEach items="${generi}" var="genere">
                            <option value="${genere}">${genere}</option>
                        </c:forEach>
                    </select>
                    <label for="categoria">Categoria</label>
                    <select name="categoria" id="categoria">
                        <option label="---"></option>
                        <c:forEach items="${categorie}" var="categoria">
                            <option value="${categoria}">${categoria}</option>
                        </c:forEach>
                    </select>
                    <label for="livello">Livello</label>
                    <select name="livello" id="livello">
                        <option label="---"></option>
                        <c:forEach items="${livelli}" var="livello">
                            <option value="${livello}">${livello}</option>
                        </c:forEach>
                    </select>
                    <label for="self">Mostra i miei corsi</label>
                    <input type="checkbox" id="self" name="id-socio" value="${socio.id}">
                    <input type="submit" value="Filtra">
                </form>
            </section>
            <section class="content clearfix">
                <c:forEach items="${corsi}" var="corso">
                    <article>
                        <h1><a href="/corso/info?id=${corso.id}">${corso.genere} ${corso.categoria} ${corso.livello}</a></h1>
                        <h2>${corso.descrizione}</h2>
                    </article>
                </c:forEach>
            </section>
        </main>
    </div>
</body>
</html>
