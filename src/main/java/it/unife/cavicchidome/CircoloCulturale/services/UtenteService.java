package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.repositories.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UtenteService {
    UtenteRepository utenteRepository;

    UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }
    public boolean validateUserInfo(
            String name,
            String surname,
            String cf,
            LocalDate dob,
            String birthplace,
            String state,
            String province,
            String city,
            String street,
            String houseNumber
    ) {
        // Aggiungi controlli per stringhe vuote
        if (name == null || name.isEmpty() || surname == null || surname.isEmpty() || cf == null || cf.isEmpty() || dob == null || birthplace == null || birthplace.isEmpty() || state == null || state.isEmpty() || province == null || province.isEmpty() || city == null || city.isEmpty() || street == null || street.isEmpty() || houseNumber == null || houseNumber.isEmpty()) {
            return false;
        }

        // Controlla che nome e cognome abbiano al massimo 20 caratteri
        if (name.length() > 20 || surname.length() > 20) {
            return false;
        }

        // Controlla che il codice fiscale abbia esattamente 16 caratteri
        if (cf.length() != 16) {
            return false;
        }

        // Controlla che la data di nascita sia odierna o antecedente
        if (dob.isAfter(LocalDate.now())) {
            return false;
        }

        // Controlla che il luogo di nascita abbia al massimo 20 caratteri
        if (birthplace.length() > 20) {
            return false;
        }

        // Controlla che l'indirizzo abbia al massimo 80 caratteri
        if ((state.length() + province.length() + city.length() + street.length() + houseNumber.length()) > 80) {
            return false;
        }

        // Controlla che nome, cognome, luogo di nascita, stato, provincia, città e via siano formati solo da caratteri e non numeri
        String regex = "^[A-Za-z\\s]+$";
        if (!name.matches(regex) || !surname.matches(regex) || !birthplace.matches(regex) || !state.matches(regex) || !province.matches(regex) || !city.matches(regex) || !street.matches(regex)) {
            return false;
        }

        // Controlla che il codice fiscale sia composto sia di numeri che di lettere
        String cfRegex = "^[0-9a-zA-Z]+$";
        if (!cf.matches(cfRegex)) {
            return false;
        }

        // Se tutti i controlli passano, restituisce true
        return true;
    }

    @Transactional
    public Utente createUtente(
            String name,
            String surname,
            String cf,
            LocalDate dob,
            String birthplace,
            String state,
            String province,
            String city,
            String street,
            String houseNumber
    ){
        // Crea un nuovo utente
        Utente utente = new Utente();
        //Integer maxId = utenteRepository.findMaxId();
        //utente.setId(maxId + 1);

        utente.setNome(name);
        utente.setCognome(surname);
        utente.setCf(cf);
        utente.setDataNascita(dob);
        utente.setLuogoNascita(birthplace);
        String indirizzo = state + ", " + province + ", " + city + ", " + street + ", " + houseNumber;
        utente.setIndirizzo(indirizzo);



        return utenteRepository.save(utente);
    }

    @Transactional
    public Optional<Utente> findById(Integer utenteId) {
        return utenteRepository.findById(utenteId);
    }

    @Transactional
    public Optional<Utente> findByCf(String cf) {
        return utenteRepository.findByCf(cf);
    }
}
