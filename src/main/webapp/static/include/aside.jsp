<%@page import="it.unife.cavicchidome.CircoloCulturale.models.Socio"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
</head>
<aside class="smallright">
    <section class="title">
        <!-- TODO: sistemare encoding UTF-8 -->
        <h1>Menù socio</h1>
    </section>
    <section class="content">
        <ul>
            <li><a href="/socio/profile">Profilo</a></li>
            <!-- <li><a href="/socio/saggi">Saggi</a></li> -->
            <li><a href="/socio/corsi">Corsi</a></li>
            <li><a href="/socio/prenotazioni">Prenotazioni</a></li>
            <li><a href="/socio/biglietti">Biglietti</a></li>
        </ul>
    </section>
<% if (request.getAttribute("socioHeader") != null && ((Socio) request.getAttribute("socioHeader")).getDocente() != null) { %>
    <section class="title">
        <h1>Menù docente</h1>
    </section>
    <section class="content">
        <ul>
            <li><a href="/docente/corsi">Corsi</a></li>
            <li><a href="/docente/saggi">Saggi</a></li>
        </ul>
    </section>
<% } %>
<% if (request.getAttribute("socioHeader") != null && ((Socio) request.getAttribute("socioHeader")).getSegretario() != null) { %>
    <section class="title">
        <h1>Menù segretario</h1>
    </section>
    <section class="content">
        <ul>
            <li><a href="/segretario/soci">Soci</a></li>
            <li><a href="/segretario/saggi">Saggi</a></li>
            <li><a href="/segretario/corsi">Corsi</a></li>
            <li><a href="/segretario/prenotazioni">Prenotazioni</a></li>
            <li><a href="/segretario/biglietti">Biglietti</a></li>
            <li><a href="/segretario/sedi">Sedi</a></li>
        </ul>
    </section>
<% } %>
</aside>