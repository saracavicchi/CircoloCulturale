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
        <main class="midleft">
            <section class="title">
                <h1>Informazioni sul Biglietto</h1>
            </section>
            <section class="content">
                <h1>Biglietto per ${biglietto.idSaggio.nome}</h1>
                ${biglietto.dataOraAcquisto}
                <form action="/biglietto/modifica" method="post">
                    <input type="hidden" name="bigliettoId" value="${biglietto.id}">
                    <label for="deleted">Cancellato</label>
                    <input type="radio" id="deleted" name="deleted" value="true" <c:if test="${biglietto.deleted}">checked</c:if>>
                    <label for="notDeleted">Non cancellato</label>
                    <input type="radio" id="notDeleted" name="deleted" value="false" <c:if test="${!biglietto.deleted}">checked</c:if>>
                    <label for="pending">Pagamento in sospeso</label>
                    <input type="radio" name="pending" id="pending" value="p" <%= ((it.unife.cavicchidome.CircoloCulturale.models.Biglietto)request.getAttribute("biglietto")).getStatoPagamento().equals('p') ? "checked" : ""%>>
                    <label for="paid">Pagato</label>
                    <input type="radio" name="pending" id="paid" value="c" <%=((it.unife.cavicchidome.CircoloCulturale.models.Biglietto)request.getAttribute("biglietto")).getStatoPagamento().equals('c') ? "checked" : ""%>>
                    <input type="submit" value="Modifica">
                </form>
            </section>
        </main>
        <%@include file="/static/include/aside.jsp"%>
    </div>
</body>
</html>
