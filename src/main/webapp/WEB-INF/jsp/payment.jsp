    <%@ page import="it.unife.cavicchidome.CircoloCulturale.models.Biglietto" %>
<%@ page import="it.unife.cavicchidome.CircoloCulturale.models.Tessera" %><%--
  Created by IntelliJ IDEA.
  User: lucadomeneghetti
  Date: 06/07/2024
  Time: 10:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Circolo Culturale</title>
    <script>

        document.addEventListener("DOMContentLoaded", () => {
            const urlParams = new URLSearchParams(window.location.search);
            if (urlParams.has("biglietto-id")) {
                const form = document.querySelector("form");
                const bigliettoIdInput = document.createElement("input");
                bigliettoIdInput.type = "hidden";
                bigliettoIdInput.name = "biglietto-id";
                bigliettoIdInput.value = urlParams.get("biglietto-id");
                form.appendChild(bigliettoIdInput);
            } else if (urlParams.has("tessera-id")) {
                const form = document.querySelector("form");
                const tesseraIdInput = document.createElement("input");
                tesseraIdInput.type = "hidden";
                tesseraIdInput.name = "tessera-id";
                tesseraIdInput.value = urlParams.get("tessera-id");
                form.appendChild(tesseraIdInput);
            }
        });

        document.addEventListener("DOMContentLoaded", () => {
            const form = document.querySelector("form");
            form.addEventListener("submit", (event) => {
                const creditCardNumber = document.querySelector("#creditCardNumber");
                const expirationDate = document.querySelector("#expirationDate");
                const cvv = document.querySelector("#cvv");
                if (creditCardNumber.value.length !== 16) {
                    alert("Il numero della carta di credito deve essere lungo 16 cifre");
                    event.preventDefault();
                } else if (new Date(expirationDate.value) < new Date()) {
                    alert("La data di scadenza della carta di credito deve essere nel futuro");
                    event.preventDefault();
                } else if (cvv.value.length !== 3) {
                    alert("Il CVV deve essere lungo 3 cifre");
                    event.preventDefault();
                }
            });
        });

        document.addEventListener("DOMContentLoaded", () => {
            const urlParams = new URLSearchParams(window.location.search);
            if (urlParams.has("failed")) {
                const form = document.querySelector("form");
                const failedMessage = document.createElement("p");
                failedMessage.textContent = "Pagamento fallito";
                failedMessage.style.color = "red";
                form.appendChild(failedMessage);
            }
        });

        document.addEventListener("DOMContentLoaded", () => {
            const cancel = document.querySelector("#cancel");
            cancel.addEventListener("click", () => {
                const form = document.querySelector("form");
                const cancelInput = document.createElement("input");
                cancelInput.type = "hidden";
                cancelInput.name = "cancelled";
                cancelInput.value = "true";
                form.appendChild(cancelInput);
                form.submit();
            });
        });

        document.addEventListener("DOMContentLoaded", () => {
            const urlParams = new URLSearchParams(window.location.search);
            if (urlParams.has("redirect")) {
                const form = document.querySelector("form");
                const redirectInput = document.createElement("input");
                redirectInput.type = "hidden";
                redirectInput.name = "redirect";
                redirectInput.value = urlParams.get("redirect");
                form.appendChild(redirectInput);
            }
        });

    </script>
    <style>
        main {
            width: 50%;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid lightgray;
            border-radius: 10px;
            box-shadow: 0 0 10px lightgray;
            background-color: white;
        }

        form {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }
        body {
            background-color: #00f;
        }

    </style>
</head>
<body>
    <main>
        <section>
            <h1>Effettua il pagamento</h1>
            <p><strong>Importo:</strong> <%
                Object biglietto;
                Object tessera;
                if ((biglietto = request.getAttribute("biglietto")) != null) {
                    out.print(((Biglietto) biglietto).getBigliettoPrice());
                } else if ((tessera = request.getAttribute("tessera")) != null) {
                    out.print(((Tessera) tessera).getCosto());
                } else {
                    out.print("0");
                }
            %></p>
            <p><strong>Causale:</strong> <%
                if ((biglietto = request.getAttribute("biglietto")) != null) {
                    out.print("Biglietto per saggio " + ((Biglietto) biglietto).getIdSaggio().getNome());
                } else if ((tessera = request.getAttribute("tessera")) != null) {
                    out.print("Tessera per socio " + ((Tessera) tessera).getIdSocio().getUtente().getCf());
                } else {
                    out.print("null");
                }
            %></p>
            <form action="/payment" method="post">
                <label for="creditCardNumber">Numero carta di credito</label>
                <input type="text" id="creditCardNumber" name="creditCardNumber" required>
                <label for="expirationDate">Data di scadenza</label>
                <input type="month" id="expirationDate" name="expirationDate" required>
                <label for="cvv">CVV</label>
                <input type="text" id="cvv" name="cvv" required>
                <input type="button" id="cancel" value="Paga in sede">
                <input type="submit" value="Paga">
            </form>
        </section>
    </main>
</body>
</html>
