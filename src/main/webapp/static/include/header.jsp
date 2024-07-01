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
    </div>
    <div id="banner">
        <img src="https://static.vecteezy.com/system/resources/previews/013/224/319/non_2x/dance-party-banner-with-happy-people-and-music-free-vector.jpg" alt="banner logo with music and dance">
        <nav></nav>
    </div>
</header>