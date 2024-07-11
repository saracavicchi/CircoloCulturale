package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.exceptions.ValidationException;
import it.unife.cavicchidome.CircoloCulturale.models.PrenotazioneSala;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.repositories.PrenotazioneSalaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneSalaService {

    private final PrenotazioneSalaRepository prenotazioneSalaRepository;
    private final SocioService socioService;

    public PrenotazioneSalaService(PrenotazioneSalaRepository prenotazioneSalaRepository, SocioService socioService) {
        this.prenotazioneSalaRepository = prenotazioneSalaRepository;
        this.socioService = socioService;
    }

    @Transactional
    public List<PrenotazioneSala> getPrenotazioneBySocio(Integer idSocio) {
        return prenotazioneSalaRepository.findBySocio(idSocio);
    }

    @Transactional
    public Optional<PrenotazioneSala> getPrenotazioneById(Integer idSocio, Integer idPrenotazione) {
        Optional<PrenotazioneSala> prenotazione = prenotazioneSalaRepository.findById(idPrenotazione);
        Optional<Socio> socio = socioService.findSocioById(idSocio);
        if (prenotazione.isPresent() && socio.isPresent()) {
            if (prenotazione.get().getIdSocio().getId().equals(socio.get().getId()) || socio.get().getSegretario() != null) {
                return prenotazione;
            }
        }
        return Optional.empty();
    }

    @Transactional
    public PrenotazioneSala newPrenotazione(String description,
                                            LocalTime startTime,
                                            LocalTime endTime,
                                            LocalDate date,
                                            Integer salaId,
                                            Integer socioId) throws ValidationException, EntityNotFoundException {

        PrenotazioneSala prenotazione = validateAndParsePrenotazione(description, startTime, endTime, date, salaId, socioId);
        prenotazione.setDataOraPrenotazione(Instant.now());
        prenotazione.setDeleted(false);

        return prenotazioneSalaRepository.save(prenotazione);
    }

    public PrenotazioneSala validateAndParsePrenotazione(String description,
                                                         LocalTime startTime,
                                                         LocalTime endTime,
                                                         LocalDate date,
                                                         Integer salaId,
                                                         Integer socioId) throws ValidationException, EntityNotFoundException {
        if (description == null || description.isBlank()) {
            throw new ValidationException("La descrizione non può essere vuota");
        }

        if (startTime == null || endTime == null) {
            throw new ValidationException("L'orario di inizio e fine non possono essere vuoti");
        }

        if (startTime.isAfter(endTime)) {
            throw new ValidationException("L'orario di inizio non può essere dopo l'orario di fine");
        }

        if (date == null) {
            throw new ValidationException("La data non può essere vuota");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new ValidationException("La data non può essere nel passato");
        }

        if (salaId == null) {
            throw new ValidationException("La sala non può essere vuota");
        }

        if (socioId == null) {
            throw new ValidationException("Il socio non può essere vuoto");
        }

        Optional<Socio> socio = socioService.findSocioById(socioId);
        if (socio.isEmpty()) {
            throw new EntityNotFoundException("Il socio non esiste");
        }

        PrenotazioneSala prenotazione = new PrenotazioneSala();
        prenotazione.setDescrizione(description);
        prenotazione.setOraInizio(startTime);
        prenotazione.setOraFine(endTime);
        prenotazione.setData(date);
        prenotazione.setIdSala(salaId);
        prenotazione.setIdSocio(socio.get());

        return prenotazione;
    }
}
