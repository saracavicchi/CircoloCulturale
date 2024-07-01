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
    <script>
        function viewSignup() {
            window.location.href = "/signup"
        }

        function onLoadHandler() {
            document.authForm.signup.addEventListener("click", )
        }

        window.addEventListener("load", onLoadHandler);
    </script>
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
            box-shadow: 0 3px 2px #777;
        }
    </style>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <main>
        <section class="title">
            <h1>Le nostre sedi</h1>
        </section>
        <section class="content clearfix">
            <c:forEach items="${sedi}" var="sede">
                <article>
                    <h1>${sede.nome}</h1>
                    <h2>${sede.indirizzo}</h2>
                </article>
            </c:forEach>
        </section>
    </main>
</body>
</html>
