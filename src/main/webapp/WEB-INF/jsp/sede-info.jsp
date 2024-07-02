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
        main {
            width: 900px;
            background: var(--content-bg);
            margin: 0 auto;
        }

        main>section.title {
            width: 100%;
            height: 35px;
            background: var(--foreground);
        }

        main>section.title>h1 {
            color: white;
            height: 100%;
            width: max-content;
            font-weight: bold;
            font-size: 1.5em;
            align-content: center;
            margin: 0 0 0 10px;
        }

        section.content {
            padding: 10px;
        }

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
            <h1>Informazioni sulla sede</h1>
        </section>
        <section class="content">
            <h1>${sede.nome}</h1>
            ${sede.indirizzo}
            ${sede.segretario.socio.utente.nome} ${sede.segretario.socio.utente.cognome}
        </section>
    </main>
</body>
</html>
