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
                <h1>Il nostro circolo culturale</h1>
            </section>
            <section class="content">
                <h1>
                </h1>
            </section>
        </main>
        <aside class="smallright">
            <section class="title">
                <h1>Prossimi saggi</h1>
            </section>
            <section class="content">
                <ul>
                    <c:forEach items="${saggi}" var="saggio">
                        <li><a href="/saggio/info?id=${saggio.id}">${saggio.nome}</a></li>
                    </c:forEach>
                </ul>
                <a href="/saggio/info">Vedi tutti i saggi</a>
            </section>
        </aside>
    </div>
</body>
</html>
