package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.Biglietto;
import it.unife.cavicchidome.CircoloCulturale.models.Saggio;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.repositories.BigliettoRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class BigliettoService {

    private final BigliettoRepository bigliettoRepository;
    private final SaggioService saggioService;
    private final UtenteService utenteService;

    public BigliettoService(BigliettoRepository bigliettoRepository, SaggioService saggioService, UtenteService utenteService) {
        this.bigliettoRepository = bigliettoRepository;
        this.saggioService = saggioService;
        this.utenteService = utenteService;
    }

    public Boolean validateBiglietto (Saggio saggio, Integer quantita, Instant dataOraAcquisto, Character statoPagamento, Boolean sconto, Boolean deleted) {
        if (quantita == null || dataOraAcquisto == null || statoPagamento == null || sconto == null || deleted == null) {
            return false;
        }

        if (quantita > saggioService.getAvailableTickets(saggio)) {
            return false;
        }

        if (dataOraAcquisto.isAfter(Instant.now())) {
            return false;
        }

        if (statoPagamento != 'c' && statoPagamento != 'p') {
            return false;
        }

        return true;
    }

    @Transactional
    public Biglietto newBiglietto(Optional<Integer> socioId,
                                   Optional<String> name,
                                   Optional<String> surname,
                                   Optional<String> cf,
                                   Optional<LocalDate> dob,
                                   Optional<String> birthplace,
                                   Optional<String> country,
                                   Optional<String> province,
                                   Optional<String> city,
                                   Optional<String> street,
                                   Optional<String> houseNumber,
                                   Integer quantity,
                                   Integer saggioId,) {

    }

}
