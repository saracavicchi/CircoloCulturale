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

    Utente validateAndParseUtente(String name,
                                  String surname,
                                  String cf,
                                  LocalDate dob,
                                  String pob,
                                  String country,
                                  String province,
                                  String city,
                                  String street,
                                  String houseNumber) throws ValidationException {

        // Aggiungi controlli per stringhe vuote
        if (name == null || name.isEmpty()
                || surname == null || surname.isEmpty()
                || cf == null || cf.isEmpty()
                || dob == null || pob == null || pob.isEmpty()
                || country == null || country.isEmpty()
                || province == null || province.isEmpty()
                || city == null || city.isEmpty()
                || street == null || street.isEmpty()
                || houseNumber == null || houseNumber.isEmpty()) {
            throw new ValidationException("Campi obbligatori non compilati");
        }

        // Controlla che nome e cognome abbiano al massimo 20 caratteri
        if (name.length() > 20 || surname.length() > 20) {
            throw new ValidationException("Nome o cognome troppo lunghi");
        }

        // Controlla che il codice fiscale abbia esattamente 16 caratteri
        if (cf.length() != 16) {
            throw new ValidationException("Il codice fiscale deve essere composto da 16 caratteri");
        }

        // Controlla che la data di nascita sia odierna o antecedente
        if (dob.isAfter(LocalDate.now())) {
            throw new ValidationException("Data di nascita successiva alla data attuale");
        }

        // Controlla che il luogo di nascita abbia al massimo 20 caratteri
        if (pob.length() > 20) {
            throw new ValidationException("Luogo di nascita troppo lungo");
        }

        // Controlla che l'indirizzo abbia al massimo 80 caratteri
        if ((country.length() + province.length() + city.length() + street.length() + houseNumber.length() ) > 80) {
            throw new ValidationException("Indirizzo troppo lungo");
        }

        // Controlla che nome, cognome, luogo di nascita, stato, provincia, città e via siano formati solo da caratteri e non numeri
        String regex = "^[A-Za-z\\s]+$";
        if (!name.matches(regex) || !surname.matches(regex) || !pob.matches(regex) || !country.matches(regex) || !province.matches(regex) || !city.matches(regex) || !street.matches(regex)) {
            throw new ValidationException("Campi non validi");
        }

        String houseNumberRegex = "^[0-9a-zA-Z]+$";
        if (!houseNumber.matches(houseNumberRegex)) {
            throw new ValidationException("Numero civico non valido");
        }

        // Controlla che il codice fiscale sia composto sia di numeri che di lettere
        String cfRegex = "^[0-9a-zA-Z]+$";
        if (!cf.matches(cfRegex)) {
            throw new ValidationException("Codice fiscale non valido");
        }

        return new Utente(cf, dob, pob, name, surname, country + ", " + province + ", " + city + ", " + street + ", " + houseNumber, false);
    }

    @Transactional
    public Utente newUtente(String name,
                            String surname,
                            String cf,
                            LocalDate dob,
                            String pob,
                            String country,
                            String province,
                            String city,
                            String street,
                            String houseNumber) throws ValidationException, EntityAlreadyPresentException {
        Utente alreadyPresent = utenteRepository.findByCf(cf);
        if (alreadyPresent != null) {
            throw new EntityAlreadyPresentException(alreadyPresent);
        }

        Utente utente = validateAndParseUtente(name, surname, cf, dob, pob, country, province, city, street, houseNumber);
        utente.setDeleted(false);

        return utenteRepository.save(utente);
    }

    @Transactional
    public Optional<Utente> findById(Integer utenteId) {
        return utenteRepository.findById(utenteId);
    }
}
