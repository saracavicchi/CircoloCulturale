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
        #main-content {
            width: 900px;
            margin: 0 auto;
        }

        main {
            float: left;
            width: 690px;
        }

        section {
            width: 100%;
        }

        section.title {
            height: 35px;
            background: var(--foreground);
        }

        section.title>h1 {
            color: white;
            height: 100%;
            width: max-content;
            font-weight: bold;
            font-size: 1.5em;
            align-content: center;
            margin: 0 0 0 10px;
        }

        section.content {
            width: calc(100% - 20px);
            padding: 10px;
            background: var(--content-bg);
        }

        aside {
            float: right;
            width: 200px;
        }
    </style>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content">
        <main class="clearfix">
            <section class="title">
                <h1>Il nostro circolo culturale</h1>
            </section>
            <section class="content">
                <h1>
                </h1>
            </section>
        </main>
        <aside>
            <section class="title">
                <h1>Prossimi saggi</h1>
            </section>
            <section class="content">
                <ul>
                    <c:forEach items="${saggi}" var="saggio">
                        <li>${saggio.nome}</li>
                    </c:forEach>
                </ul>
            </section>
        </aside>
    </div>
</body>
</html>
