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
    <div id="main-content" class="clearfix">
        <main class="midleft">
            <section class="title">
                <h1>Informazioni sul Biglietto</h1>
            </section>
            <section class="content">
                <h1>Biglietto per ${biglietto.idSaggio.nome}</h1>
                <p><strong>Acquistato in data:</strong> ${biglietto.dataOraAcquisto}</p>
                <p><strong>Acquistato da:</strong> ${biglietto.idUtente.nome} ${biglietto.idUtente.cognome} (${biglietto.idUtente.cf})</p>
                <p><strong>Quantità:</strong> ${biglietto.quantita}</p>
                <form action="/biglietto/modifica" method="post">
                    <input type="hidden" name="bigliettoId" value="${biglietto.id}">
                    <fieldset>
                        <legend>Stato biglietto</legend>
                    <label for="deleted">Cancellato</label>
                    <input type="radio" id="deleted" name="deleted" value="true" <c:if test="${biglietto.deleted}">checked</c:if>>
                    <label for="notDeleted">Non cancellato</label>
                    <input type="radio" id="notDeleted" name="deleted" value="false" <c:if test="${!biglietto.deleted}">checked</c:if>>
                    </fieldset>
                    <fieldset>
                        <legend>Stato pagamento</legend>
                    <label for="pending">Pagamento in sospeso</label>
                    <input type="radio" name="pending" id="pending" value="p" <%= ((it.unife.cavicchidome.CircoloCulturale.models.Biglietto)request.getAttribute("biglietto")).getStatoPagamento().equals('p') ? "checked" : ""%>>
                    <label for="paid">Pagato</label>
                        <input type="radio" name="pending" id="paid" value="c" <%=((it.unife.cavicchidome.CircoloCulturale.models.Biglietto)request.getAttribute("biglietto")).getStatoPagamento().equals('c') ? "checked" : ""%>>
                    </fieldset>
                    <input type="submit" value="Modifica">
                </form>
            </section>
        </main>
        <%@include file="/static/include/aside.jsp"%>
    </div>
    <%@include file="/static/include/footer.jsp"%>
</body>
</html>
