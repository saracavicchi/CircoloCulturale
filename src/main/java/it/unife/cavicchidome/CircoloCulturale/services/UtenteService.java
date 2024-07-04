package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.exceptions.EntityAlreadyPresentException;
import it.unife.cavicchidome.CircoloCulturale.exceptions.ValidationException;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.repositories.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UtenteService {
    UtenteRepository utenteRepository;

    UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    Utente validateUtente(Utente utente) throws ValidationException {

        // Aggiungi controlli per stringhe vuote
        if (utente.getNome() == null || utente.getNome().isEmpty()
                || utente.getCognome() == null || utente.getCognome().isEmpty()
                || utente.getCf() == null || utente.getCf().isEmpty()
                || utente.getDataNascita() == null || utente.getLuogoNascita() == null || utente.getLuogoNascita().isEmpty()
                || utente.getIndirizzo() == null) {
            throw new ValidationException("Campi obbligatori non compilati");
        }

        String[] indirizzoSplitted = utente.getIndirizzo().split(", ");
        for (String s : indirizzoSplitted) {
            if (s.isEmpty()) {
                throw new ValidationException("Campi obbligatori non compilati");
            }
        }

        // Controlla che nome e cognome abbiano al massimo 20 caratteri
        if (utente.getNome().length() > 20 || utente.getCognome().length() > 20) {
            throw new ValidationException("Nome o cognome troppo lunghi");
        }

        // Controlla che il codice fiscale abbia esattamente 16 caratteri
        if (utente.getCf().length() != 16) {
            throw new ValidationException("Il codice fiscale deve essere composto da 16 caratteri");
        }

        // Controlla che la data di nascita sia odierna o antecedente
        if (utente.getDataNascita().isAfter(LocalDate.now())) {
            throw new ValidationException("Data di nascita successiva alla data attuale");
        }

        // Controlla che il luogo di nascita abbia al massimo 20 caratteri
        if (utente.getLuogoNascita().length() > 20) {
            throw new ValidationException("Luogo di nascita troppo lungo");
        }

        // Controlla che l'indirizzo abbia al massimo 80 caratteri
        if (utente.getIndirizzo().length() > 80) {
            throw new ValidationException("Indirizzo troppo lungo");
        }

        // Controlla che nome, cognome, luogo di nascita, stato, provincia, città e via siano formati solo da caratteri e non numeri
        String regex = "^[A-Za-z\\s]+$";
        if (!utente.getNome().matches(regex) || !utente.getCognome().matches(regex) || !utente.getLuogoNascita().matches(regex) || !indirizzoSplitted[0].matches(regex) || !indirizzoSplitted[1].matches(regex) || !indirizzoSplitted[2].matches(regex) || !indirizzoSplitted[3].matches(regex)) {
            throw new ValidationException("Campi non validi");
        }

        // Controlla che il codice fiscale sia composto sia di numeri che di lettere
        String cfRegex = "^[0-9a-zA-Z]+$";
        if (!utente.getCf().matches(cfRegex)) {
            throw new ValidationException("Codice fiscale non valido");
        }

        return utente;
    }

    @Transactional
    public Utente newUtente(String nome,
                                String cognome,
                                String cf,
                                LocalDate dob,
                                String luogoNascita,
                                String indirizzo) throws ValidationException, EntityAlreadyPresentException {

        if (utenteRepository.findByCf(cf) != null) {
            throw new EntityAlreadyPresentException("Utente già presente nel database");
        }

        Utente utente = new Utente(cf, dob, luogoNascita, nome, cognome, indirizzo, false);

        return utenteRepository.save(validateUtente(utente));
    }

    @Transactional
    public Optional<Utente> findById(Integer utenteId) {
        return utenteRepository.findById(utenteId);
    }
}
