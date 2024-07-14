package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.exceptions.ValidationException;
import it.unife.cavicchidome.CircoloCulturale.models.PrenotazioneSala;
import it.unife.cavicchidome.CircoloCulturale.models.Sala;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.models.Weekday;
import it.unife.cavicchidome.CircoloCulturale.repositories.PrenotazioneSalaRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.SalaRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
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
    private final CorsoService corsoService;
    private final SedeService sedeService;
    private final SalaRepository salaRepository;
    private final SocioRepository socioRepository;
    private final SalaService salaService;

    public PrenotazioneSalaService(PrenotazioneSalaRepository prenotazioneSalaRepository, SocioService socioService, CorsoService corsoService, SedeService sedeService, SalaRepository salaRepository, SocioRepository socioRepository, SalaService salaService) {
        this.prenotazioneSalaRepository = prenotazioneSalaRepository;
        this.socioService = socioService;
        this.corsoService = corsoService;
        this.sedeService = sedeService;
        this.salaRepository = salaRepository;
        this.socioRepository = socioRepository;
        this.salaService = salaService;
    }

    @Transactional
    public List<PrenotazioneSala> getPrenotazioneBySocio(Integer idSocio, Optional<LocalDate> date) {
        return prenotazioneSalaRepository.findBySocio(idSocio, date.orElse(LocalDate.now()));
    }

    @Transactional
    public List<PrenotazioneSala> getPrenotazioneBySalaAfterDataDeleted(Optional<Integer> idSala, Optional<LocalDate> data, Optional<Boolean> deleted) {
        if (idSala.isPresent()) {
            return prenotazioneSalaRepository.findBySalaAndAfterDataDeleted(idSala.get(), data.orElse(LocalDate.now()), deleted.orElse(false));
        } else {
            return prenotazioneSalaRepository.findAfterDataDeleted(data.orElse(LocalDate.now()), deleted.orElse(false));
        }
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

    public boolean prenotazioneOverlap(Integer salaId, LocalDate date, LocalTime start, LocalTime end) {
        return prenotazioneSalaRepository.findOverlapPrenotazione(salaId, date, start, end).isPresent();
    }

    @Transactional
    public PrenotazioneSala newPrenotazione(String description,
                                            LocalTime startTime,
                                            LocalTime endTime,
                                            LocalDate date,
                                            Integer salaId,
                                            Integer socioId) throws ValidationException, EntityNotFoundException {

        PrenotazioneSala prenotazione = validateAndParsePrenotazione(description, startTime, endTime, date, salaId, socioId);

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

        Optional<Sala> sala = salaService.findById(salaId);
        if (sala.isEmpty()) {
            throw new EntityNotFoundException("La sala non esiste");
        }

        if (prenotazioneOverlap(salaId, date, startTime, endTime) ||
                corsoService.corsoOverlap(salaId, date, startTime, endTime) ||
                sedeService.outsideOpeningHours(salaId, Weekday.fromDayNumber(date.getDayOfWeek().getValue()), startTime, endTime)) {
            throw new ValidationException("La sala è già prenotata in quell'orario");
        }

        PrenotazioneSala prenotazione = new PrenotazioneSala();
        prenotazione.setDescrizione(description);
        prenotazione.setOrarioInizio(startTime);
        prenotazione.setOrarioFine(endTime);
        prenotazione.setData(date);
        prenotazione.setIdSala(sala.get());
        prenotazione.setIdSocio(socio.get());
        prenotazione.setDeleted(false);
        prenotazione.setDataOraPrenotazione(Instant.now());

        return prenotazione;
    }

    @Transactional
    public void deletePrenotazione(Integer idSocio, Integer idPrenotazione) throws ValidationException, EntityNotFoundException {
        PrenotazioneSala prenotazione = prenotazioneSalaRepository.getReferenceById(idPrenotazione);
        Socio socio = socioRepository.getReferenceById(idSocio);
        if (socio.getSegretario() == null && !prenotazione.getIdSocio().getId().equals(socio.getId())) {
            throw new ValidationException("Non hai i permessi per cancellare questa prenotazione");
        } else {
            prenotazione.setDeleted(true);
            prenotazioneSalaRepository.save(prenotazione);
        }
    }

    @Transactional
    public List<PrenotazioneSala> findByDate(LocalDate date) {
        return prenotazioneSalaRepository.findByDate(date);
    }
}
