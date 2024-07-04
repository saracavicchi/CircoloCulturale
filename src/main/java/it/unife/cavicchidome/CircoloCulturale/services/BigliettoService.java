package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.exceptions.EntityAlreadyPresentException;
import it.unife.cavicchidome.CircoloCulturale.exceptions.ValidationException;
import it.unife.cavicchidome.CircoloCulturale.models.Biglietto;
import it.unife.cavicchidome.CircoloCulturale.models.Saggio;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.repositories.BigliettoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class BigliettoService {

    private final BigliettoRepository bigliettoRepository;
    private final SaggioService saggioService;
    private final UtenteService utenteService;
    private final SocioService socioService;

    public BigliettoService(BigliettoRepository bigliettoRepository, SaggioService saggioService, UtenteService utenteService, SocioService socioService) {
        this.bigliettoRepository = bigliettoRepository;
        this.saggioService = saggioService;
        this.utenteService = utenteService;
        this.socioService = socioService;
    }

    public Biglietto validateAndParseBiglietto (Saggio saggio, Integer quantita, Instant dataOraAcquisto, Character statoPagamento, Boolean sconto, Boolean deleted)
                                                throws ValidationException {
        if (quantita == null || dataOraAcquisto == null || statoPagamento == null || sconto == null || deleted == null) {
            throw new ValidationException("Dati biglietto non validi");
        }

        if (quantita > saggioService.getAvailableTickets(saggio)) {
            throw new ValidationException("Biglietti non disponibili");
        }

        if (dataOraAcquisto.isAfter(Instant.now())) {
            throw new ValidationException("Data di acquisto non valida");
        }

        if (statoPagamento != 'c' && statoPagamento != 'p') {
            throw new ValidationException("Stato pagamento non valido");
        }

        return new Biglietto(quantita, dataOraAcquisto, statoPagamento, sconto, deleted);
    }

    public Biglietto validateBiglietto(Biglietto biglietto) throws ValidationException{
        return validateAndParseBiglietto(biglietto.getIdSaggio(), biglietto.getQuantita(), biglietto.getDataOraAcquisto(), biglietto.getStatoPagamento(), biglietto.getSconto(), biglietto.getDeleted());
    }

    public Biglietto purchaseBiglietto(Integer bigliettoId) throws EntityNotFoundException {
        Biglietto updateBiglietto = bigliettoRepository.getReferenceById(bigliettoId);
        updateBiglietto.setStatoPagamento('c');
        return bigliettoRepository.save(updateBiglietto);
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
                                   Integer saggioId) throws ValidationException, EntityNotFoundException {

        Utente utente;
        if (socioId.isPresent()) {
            Socio socio = socioService.findSocioById(socioId.get()).orElseThrow(() -> new EntityNotFoundException("Socio non trovato"));
            utente = socio.getUtente();
        } else {
            if (name.isPresent() && surname.isPresent() && cf.isPresent() && dob.isPresent() && birthplace.isPresent() && country.isPresent() && province.isPresent() && city.isPresent() && street.isPresent() && houseNumber.isPresent()) {
                try{
                    utente = utenteService.newUtente(name.get(), surname.get(), cf.get(), dob.get(), birthplace.get(), country.get(), province.get(), city.get(), street.get(), houseNumber.get());
                } catch(EntityAlreadyPresentException exc) {
                    utente = exc.getEntity();
                }
            } else {
                throw new ValidationException("Dati utente non validi");
            }
        }

        Saggio saggio = saggioService.findSaggioById(saggioId).orElseThrow(() -> new EntityNotFoundException("Saggio non trovato"));

        Biglietto biglietto = validateAndParseBiglietto(saggio, quantity, Instant.now(), 'p', (utente.getSocio() != null), false);
        biglietto.setIdUtente(utente);
        biglietto.setIdSaggio(saggio);

        return bigliettoRepository.save(biglietto);
    }

    public Optional<Biglietto> findBigliettoById(Integer id) {
        return bigliettoRepository.findById(id);
    }

}
