<%--
  Created by IntelliJ IDEA.
  User: sarac
  Date: 16/07/2024
  Time: 21:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>Circolo La Sinfonia</title>
    <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <style>
        .map {
            margin-top: 20px;
        }

        #map {
            display: flex;
            justify-content: center;
            align-items: center;
            width: 670px;
            margin-right: 30px;
            margin-left: -10px;
            height: 400px;
        }
    </style>
    <script>
        const urlParams = new URLSearchParams(window.location.search);
        const authFailed = urlParams.get('authFailed');
        if(authFailed === 'true'){
            alert('Autenticazione fallita. Nel caso in cui si tratti di un\'utenza con tessera non confermata, si prega di rivolgersi ad una delle nostre segreterie');
        }
    </script>
</head>
<script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>

</head>
<body>
<%@include file="/static/include/header.jsp"%>
<div id="main-content" class="clearfix">
    <main class="fullsize clearfix">
        <section class="title">
            <h1>Contattaci</h1>
        </section>
        <section class="content">
            <p>Per qualsiasi domanda o informazione, contattaci utilizzando i seguenti dettagli:</p>
            <div class="contact-info">
                <p>Email: <a href="mailto:circoloculturalecd@gmail.com">circoloculturalecd@gmail.com</a></p>
                <p>Telefono: <a href="tel:+393457628530">+39 345 762 8530</a></p>
                <p>Indirizzo: "${sede.indirizzo}</p>
            </div>
            <div class="map">
                <div id="map"></div>
            </div>
        </section>
    </main>
</div>
<%@include file="/static/include/footer.jsp"%>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        var address= "${sede.indirizzo}";
        var map = L.map('map').setView([44.6919, 12.1821], 15);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        }).addTo(map);

        // Funzione per geocodificare un indirizzo e posizionare il marker sulla mappa
        function geocodeAddress(address) {
            var encodedAddress = encodeURIComponent(address);
            var apiUrl = `https://nominatim.openstreetmap.org/search?q=${sede.indirizzo}&format=json`;

            axios.get(apiUrl)
                .then(function (response) {
                    if (response.data.length > 0) {
                        var firstResult = response.data[0];
                        var lat = parseFloat(firstResult.lat);
                        var lon = parseFloat(firstResult.lon);
                        map.setView([lat, lon], 15);
                        L.marker([lat, lon]).addTo(map)
                            .bindPopup('Circolo Culturale<br>' + address)
                            .openPopup();
                    } else {
                        console.log('Indirizzo non trovato');
                    }
                })
                .catch(function (error) {
                    console.error('Errore nella richiesta di geocodifica', error);
                });
        }

        // Chiamata alla funzione di geocodifica all'avvio della pagina
        geocodeAddress(address);
    });
</script>
</body>
</html>

