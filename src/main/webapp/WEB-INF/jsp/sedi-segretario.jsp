<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 15/07/2024
  Time: 17:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>Circolo La Sinfonia</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <script>
        function redirectToCreaSedePage() {
            window.location.href = '/sede/crea';
        }
    </script>
</head>
<body>
<%@include file="/static/include/header.jsp"%>
<div id="main-content" class="clearfix">
    <main class="midleft">
        <section class="title">
            <h1>Le nostre sedi</h1>
        </section>
        <section class="content">
            <button type="button" onclick="redirectToCreaSedePage()">Crea Nuova Sede</button>
        </section>
        <section class="content clearfix">
            <c:forEach items="${sedi}" var="sede">
                <article <c:if test="${sede.active == false}">class="deleted"</c:if>>
                    <h1><a href="/sede/modifica?idSede=${sede.id}">${sede.nome}</a></h1>
                    <h2>${sede.indirizzo}</h2>
                </article>
            </c:forEach>
        </section>
    </main>
    <%@include file="/static/include/aside.jsp"%>
</div>
<%@include file="/static/include/footer.jsp"%>
</body>
</html>

