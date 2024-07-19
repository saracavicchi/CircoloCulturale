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
    <style>
        h1{
            color:  var(--border);
            text-align: center;
        }

        .image{
            display: flex;
            justify-content: center;
            align-items: center;
            width: 610px;
            margin-right: 52px;
            margin-left: 18px;
            height: 260px;
        }


        aside .content ul {
            list-style-type: none; /* Rimuove i pallini */
            padding: 0;
            margin: 0; /* Rimuove margini di default */
        }

        aside .content li{
            margin-bottom: 15px; /* Aumenta lo spazio tra gli elementi della lista */
        }

        aside .content li a{
            color: #333; /* Colore del testo dei link */
            text-decoration: none; /* Rimuove il sottolineato dei link */
            font-weight: bold; /* Rende il testo dei link in grassetto */
        }

        aside .content a:hover {
            color: #a3271f; /* Cambia il colore al passaggio del mouse */
            text-decoration: underline; /* Aggiunge un sottolineato al passaggio del mouse */
        }

        aside .content a{
            color: var(--border); /* Colore del testo dei link */
            text-decoration: none; /* Rimuove il sottolineato dei link */
            font-weight: bold; /* Rende il testo dei link in grassetto */
        }

    </style>
    <script>
        const urlParams = new URLSearchParams(window.location.search);
        const authFailed = urlParams.get('authFailed');
        if(authFailed === 'true'){
            alert('Autenticazione fallita.');
        }

        const pending = urlParams.get('pending');
        if(pending === 'true'){
            alert('La tessera non è stata ancora confermata. Si prega di rivolgersi ad una delle nostre segreterie.');
        }
    </script>
</head>
<body>
    <%@include file="/static/include/header.jsp"%>
    <div id="main-content" class="clearfix">
        <main class="clearfix midleft">
            <section class="title">
                <h1>Il nostro circolo culturale</h1>
            </section>
            <section class="content">

                <img class="image" src="/images/circoloCulturalePhotos/circolo.jpg" alt="immagine Circolo Culturale">
                <h1>Benvenuti al Circolo Culturale La Sinfonia</h1>
                <p>
                    Il nostro circolo è un punto di incontro per appassionati di danza, musica e molto altro.
                    Unisciti a noi per esplorare nuove idee, partecipare a eventi culturali e condividere le tue passioni.
                </p>
            </section>
            <section class="content">
                <h1>Chi Siamo</h1>
                <p>
                    Il nostro Circolo Culturale è stato fondato nel 1988 con l'obiettivo di promuovere
                    la cultura e l'arte nella nostra comunità. Organizziamo eventi, corsi e incontri per tutti gli amanti
                    della cultura.
                </p>
            </section>
            <section class="content">
                <h1>Unisciti a noi</h1>
                <p>
                    Sei un appassionato di danza, musica, teatro o arte? Vuoi condividere le tue passioni con altre persone?
                    Iscriviti al nostro circolo e partecipa a eventi, corsi e saggi organizzati da noi.
                    In più avrai la possibilità di utilizzare i nostri spazi per le tue attività culturali!
                    Se sei interessato, nella sezione contatti trovi tutte le informazioni per contattarci.
                </p>
            </section>
        </main>
        <aside class="smallright">
            <section class="title">
                <h1>Prossimi saggi</h1>
            </section>
            <section class="content">
                <ul>
                    <c:forEach items="${saggi}" var="saggio">
                        <li><a href="/saggio/info?id=${saggio.id}">${saggio.nome}</a></li>
                    </c:forEach>
                </ul>
                <a href="/saggio/info">Vedi tutti i saggi</a>
            </section>
        </aside>
    </div>
    <%@include file="/static/include/footer.jsp"%>
</body>

</html>
