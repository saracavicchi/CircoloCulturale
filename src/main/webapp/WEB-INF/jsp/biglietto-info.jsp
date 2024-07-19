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
    <title>Circolo La Sinfonia</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <script>
        document.addEventListener("DOMContentLoaded", () => {
            const urlParams = new URLSearchParams(window.location.search);
            if (urlParams.has("cancelled") || urlParams.has("error")) {
                const content = document.querySelector(".content");
                const failedMessage = document.createElement("p");
                failedMessage.textContent = "Pagamento fallito o respinto dall'utente";
                failedMessage.style.color = "red";
                content.appendChild(failedMessage);
            }
        });

        document.addEventListener("DOMContentLoaded", () => {
            const urlParams = new URLSearchParams(window.location.search);
            if (urlParams.has("success")) {
                const content = document.querySelector(".content");
                const successMessage = document.createElement("p");
                successMessage.textContent = "Pagamento effettuato con successo";
                successMessage.style.color = "green";
                content.appendChild(successMessage);
            }
        });
    </script>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content" class="clearfix">
        <main class="midleft">
            <section class="title">
                <h1>Informazioni sul Biglietto</h1>
            </section>
            <section class="content">
                <% if (((it.unife.cavicchidome.CircoloCulturale.models.Biglietto)request.getAttribute("biglietto")).getStatoPagamento().equals('p') ) {%>
                <p style="color: red">Biglietto in sospeso</p>
                <% } %>
                <h1>Biglietto per ${biglietto.idSaggio.nome}</h1>
                <p><strong>Acquistato in data:</strong> ${biglietto.dataOraAcquisto}</p>
                <p><strong>Acquistato da:</strong> ${biglietto.idUtente.nome} ${biglietto.idUtente.cognome} (${biglietto.idUtente.cf})</p>
                <p><strong>Quantità:</strong> ${biglietto.quantita}</p>
            </section>
        </main>
        <%@include file="/static/include/aside.jsp"%>
    </div>
    <%@include file="/static/include/footer.jsp"%>
</body>
</html>
