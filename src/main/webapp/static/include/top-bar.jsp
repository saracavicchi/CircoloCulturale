<div id="top-bar">
    <% if (request.getAttribute("socio") != null) { %>
    <p>
        ${socio.utente.nome} ${socio.utente.cognome}
    </p>
    <% } else { %>
    <form action ="/login" method="POST">
        <input name="cf" type="text" placeholder="Codice fiscale">
        <input name="password" type="password" placeholder="Password">
        <input type="submit" value="Accedi">
        <input type="button" value="Registrati">
        <script>
            function setRedirectValue() {
                let redirectField = document.getElementById("redirectField")
                redirectField.value = window.location.pathname + window.location.search
            }

            window.addEventListener("load", setRedirectValue);
        </script>
        <input type="hidden" id="redirectField" name="redirectTo" value="">
    </form>
    <% } %>
</div>