<%--
  Created by IntelliJ IDEA.
  User: lucadomeneghetti
  Date: 02/07/2024
  Time: 10:28
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
        const urlParams = new URLSearchParams(window.location.search);
        const authFailed = urlParams.get('authFailed');
        if(authFailed === 'true'){
            alert('Autenticazione fallita. Nel caso in cui si tratti di un\'utenza con tessera non confermata, si prega di rivolgersi ad una delle nostre segreterie');
        }
    </script>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content" class="clearfix">
        <main class="midleft clearfix">
            <section class="title">
                <h1>Tutti i saggi del circolo</h1>
            </section>
            <section class="filter">
                <form action="/saggio/info" method="get">
                    <label for="data">Filtra per data:</label>
                    <input type="date" name="data" id="data" value="<%= request.getParameter("data") != null ? request.getParameter("data") : java.time.LocalDate.now()%>>">
                    <input type="submit" value="Filtra"/>
                </form>
            </section>
            <section class="content clearfix">
                <c:forEach items="${saggi}" var="saggio">
                    <article class="full">
                        <h1><a href="/saggio/info?id=${saggio.id}">${saggio.nome}</a></h1>
                        <h2>${saggio.descrizione}</h2>
                        <p>${saggio.data} - ${saggio.orarioInizio}
                        ${saggio.indirizzo}</p>
                    </article>
                </c:forEach>
            </section>
        </main>
        <%@include file="/static/include/aside.jsp"%>
    </div>
    <%@include file="/static/include/footer.jsp"%>
</body>
</html>
