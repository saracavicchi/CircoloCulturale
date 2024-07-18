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
    <style>
        section.content article {
            float: left;
            width: 250px;
            border-width: 1px;
            border-style: solid;
            border-radius: 10px;
            border-color: #a3271f;
            padding: 10px 8px 10px 20px;
            margin: 0 18px 16px 0;
            background: linear-gradient(to right,#fdfbfb,#ebedee);
        }
    </style>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content" class="clearfix">
        <main class="full">
            <section class="title">
                <h1>Le nostre sedi</h1>
            </section>
            <section class="content clearfix">
                <c:forEach items="${sedi}" var="sede">
                    <article <c:if test="${sede.active == false}">class="deleted"</c:if>>
                        <h1><a href="/sede/info?id=${sede.id}">${sede.nome}</a></h1>
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
