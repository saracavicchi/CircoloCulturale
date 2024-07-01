package it.unife.cavicchidome.CircoloCulturale.services;

import org.springframework.stereotype.Service;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.models.Tessera;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.repositories.TesseraRepository;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.time.LocalDate;
import java.math.BigDecimal;

@Service
public class TesseraService {

    private final TesseraRepository tesseraRepository;

    public TesseraService(TesseraRepository tesseraRepository) {
        this.tesseraRepository = tesseraRepository;
    }

    @Transactional
    public Tessera createTessera(Socio socio) {
        long timestamp = Instant.now().getEpochSecond();
        long id = socio.getId() ; // assuming there is a getId() method

        String tesseraId = Long.toString(id, 36) + Long.toString(timestamp, 36);

        // Truncate or pad with zeros in case the ID is not 10 characters
        if (tesseraId.length() > 10) {
            tesseraId = tesseraId.substring(0, 10);
        } else while (tesseraId.length() < 10) {
            tesseraId = tesseraId + "0";
        }

        Tessera tessera = new Tessera();
        tessera.setId(tesseraId);
        tessera.setIdSocio(socio);
        tessera.setDataEmissione(LocalDate.now());
        tessera.setCosto(BigDecimal.valueOf(10.0)); //capire come fare il costo
        tessera.setStatoPagamento('p'); //da cambiare



        return tesseraRepository.save(tessera);
    }
}
