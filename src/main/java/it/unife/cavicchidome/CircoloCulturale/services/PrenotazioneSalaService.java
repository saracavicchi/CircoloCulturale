package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.exceptions.ValidationException;
import it.unife.cavicchidome.CircoloCulturale.models.*;
import it.unife.cavicchidome.CircoloCulturale.repositories.PrenotazioneSalaRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.SalaRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
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
import java.util.Properties;

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
    public List<PrenotazioneSala> getPrenotazioneBySocio(Integer idSocio, Optional<LocalDate> date) { //solo prenotazioni e soci active
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
    public Optional<PrenotazioneSala> getPrenotazioneById(Integer idSocio, Integer idPrenotazione) {//TODO: Serve controllo su prenotazioni/soci attivi? Attenzione è anche usato dal segretario che vede anche cancellate
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

        newPrenotazioneEmail(prenotazione.getIdSocio(), prenotazione);

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
            deletePrenotazioneEmail(prenotazione.getIdSocio(), prenotazione);
            prenotazioneSalaRepository.save(prenotazione);
        }
    }

    @Transactional
    public List<PrenotazioneSala> findByDateAndSede(LocalDate date, Sede sede) {
        return prenotazioneSalaRepository.findByDateAndSede(date, sede);
    }

    @Transactional
    public List<PrenotazioneSala> findBySala(Integer idSala) {
        return prenotazioneSalaRepository.findBySala(idSala);
    }

    @Transactional
    public List<PrenotazioneSala> findBySedeId(Integer idSede) {
        return prenotazioneSalaRepository.findBySedeId(idSede);
    }

    public void newPrenotazioneEmail(Socio socio, PrenotazioneSala prenotazione) {
        final String username = "circoloculturaleCD@gmail.com";
        final String password = "fcqn ntzj hzsw agnu"; // replace with your password

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("circoloculturaleCD@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(socio.getEmail())
            );
            message.setSubject("Nuova prenotazione sala");
            message.setText("Gentile " + socio.getUtente().getNome() + ","
                    + "\n\nLe confermiamo l'avvenuta prenotazione della sala " + prenotazione.getIdSala().getNumeroSala() + " (sede " + prenotazione.getIdSala().getIdSede().getNome() +
                    ") per il giorno " + prenotazione.getData() + " dalle ore " + prenotazione.getOrarioInizio() + " alle ore " + prenotazione.getOrarioFine() + "." +
                    "\n\nIl suo codice prenotazione è: " + prenotazione.getId() +
                    "\n\nCordiali saluti,\nCircolo Culturale \"La Sinfonia\"");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void deletePrenotazioneEmail(Socio socio, PrenotazioneSala prenotazione) {
        final String username = "circoloculturaleCD@gmail.com";
        final String password = "fcqn ntzj hzsw agnu"; // replace with your password

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("circoloculturaleCD@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(socio.getEmail())
            );
            message.setSubject("Prenotazione sala cancellata");
            message.setText("Gentile " + socio.getUtente().getNome() + ","
                    + "\n\nLe comunichiamo la cancellazione della prenotazione numero " + prenotazione.getId() + "." +
                    "\n\nLa prenotazione era fissata in data " + prenotazione.getData() + " dalle ore " + prenotazione.getOrarioInizio() + " alle ore " + prenotazione.getOrarioFine() +
                            " per la sala " + prenotazione.getIdSala().getNumeroSala() + " (sede " + prenotazione.getIdSala().getIdSede().getNome() + ")." +
                    "\n\nCordiali saluti,\nCircolo Culturale \"La Sinfonia\"");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
