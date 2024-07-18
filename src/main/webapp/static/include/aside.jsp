<%@page import="it.unife.cavicchidome.CircoloCulturale.models.Socio"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<aside class="smallright">
    <% if (request.getAttribute("socioHeader") != null) { %>
    <section class="title">
        <h1>Menù socio</h1>
    </section>
    <section class="content">
        <ul>
            <li><button onclick="window.location.href='/socio/profile';">Profilo</button></li>
            <li><button onclick="window.location.href='/socio/corsi';">Corsi</button></li>
            <li><button onclick="window.location.href='/socio/prenotazioni';">Prenotazioni</button></li>
            <li><button onclick="window.location.href='/socio/biglietti';">Biglietti</button></li>
        </ul>
    </section>
<% } %>
<% if (request.getAttribute("socioHeader") != null && ((Socio) request.getAttribute("socioHeader")).getDocente() != null) { %>
    <section class="title">
        <h1>Menù docente</h1>
    </section>
    <section class="content">
        <ul>
            <li><button onclick="window.location.href='/docente/corsi';">Corsi Insegnati</button></li>
            <!--<li><a href="/docente/lezioni/aggiungi">Aggiungi Lezione</a></li> -->
        </ul>
    </section>
<% } %>
<% if (request.getAttribute("socioHeader") != null && ((Socio) request.getAttribute("socioHeader")).getSegretario() != null) { %>
    <section class="title">
        <h1>Menù segretario</h1>
    </section>
    <section class="content">
        <ul>
            <li><button onclick="location.href='/segretario/soci';">Soci</button></li>
            <li><button onclick="location.href='/segretario/saggi';">Saggi</button></li>
            <li><button onclick="location.href='/segretario/corsi';">Corsi</button></li>
            <li><button onclick="location.href='/segretario/prenotazioni';">Prenotazioni</button></li>
            <li><button onclick="location.href='/segretario/biglietti';">Biglietti</button></li>
            <li><button onclick="location.href='/segretario/sedi';">Sedi</button></li>
        </ul>
    </section>
<% } %>
</aside>