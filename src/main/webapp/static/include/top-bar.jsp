<header class="clearfix">
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
            <input type="hidden" name="redirectTo" value="${requestScope['javax.servlet.forward.request_uri']}">
        </form>
        <% } %>
</header>