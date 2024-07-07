<aside class="smallright">
    <section class="title">
        <h1>Menù socio</h1>
    </section>
    <section class="content">
        <ul>
            <li><a href="/socio/profile">Profilo</a></li>
            <li><a href="/socio/saggi">Saggi</a></li>
            <li><a href="/socio/corsi">Corsi</a></li>
            <li><a href="/socio/prenotazioni">Prenotazioni</a></li>
        </ul>
    </section>
</aside>
<% if (request.getAttribute("socio") != null && ((Socio) request.getAttribute("socio")).getDocente() != null) { %>
<aside class="smallright">
    <section class="title">
        <h1>Menù docente</h1>
    </section>
    <section class="content">
        <ul>
            <li><a href="/docente/lezioni">Lezioni</a></li>
            <li><a href="/docente/lezioni/aggiungi">Aggiungi Lezione</a></li>
        </ul>
    </section>
</aside>
<% } %>
<% if (request.getAttribute("socio") != null && ((Socio) request.getAttribute("socio")).getSegretario() != null) { %>
<aside class="smallright">
    <section class="title">
        <h1>Menù segretario</h1>
    </section>
    <section class="content">
        <ul>
            <li><a href="/segretario/soci">Soci</a></li>
            <li><a href="/segretario/saggi">Saggi</a></li>
            <li><a href="/segretario/corsi">Corsi</a></li>
            <li><a href="/segretario/prenotazioni">Prenotazioni</a></li>
        </ul>
    </section>
</aside>
<% } %>