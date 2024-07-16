<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 16/07/2024
  Time: 11:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Circolo Culturale</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css">
    <script>

    </script>
</head>
<body>
<%@include file="/static/include/header.jsp"%>
<div id="main-content">
    <main class="midleft">
        <section class="title">
            <h1>Corsi del circolo</h1>
        </section>
        <section class="content clearfix">
            <c:forEach items="${corsi}" var="corso">
                <article>
                    <h1><a href="/corso/info?id=${corso.id}&isDocente=true">${corso.categoria} ${corso.genere} ${corso.livello}</a></h1>
                    <h2>${corso.descrizione}</h2>
                </article>
            </c:forEach>
        </section>
    </main>
    <%@include file="/static/include/aside.jsp"%>
</div>
</body>
</html>
