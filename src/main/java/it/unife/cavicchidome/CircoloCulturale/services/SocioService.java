package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.UtenteRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class SocioService {

    SocioRepository socioRepository;

    SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Transactional
    public Optional<Integer> authenticate(String cf, String password) {
        Optional<Socio> socio = socioRepository.authenticateSocio(cf, password);
        if (socio.isPresent()) {
            return Optional.of(socio.get().getId());
        } else {
            return Optional.empty();
        }
    }

    public boolean validateSocioInfo(
            String email,
            String password,
            String phoneNumber
            //String photoUrl
    ) {
        // Crea un'istanza di EmailValidator
        EmailValidator emailValidator = EmailValidator.getInstance();

        // Controlla se l'email è un'email valida e non supera i 50 caratteri
        if (email == null || email.length() > 50 || !emailValidator.isValid(email)) {
            return false;
        }

        // Controlla se la password ha almeno 8 caratteri, almeno una lettera maiuscola, una lettera minuscola, un numero e non supera i 50 caratteri
        if (password == null || password.length() > 50 || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,50}$")) {
            return false;
        }

        // Controlla se il numero di telefono contiene solo numeri e ha esattamente 10 cifre
        if (phoneNumber != null && !phoneNumber.isEmpty()){
            if( !phoneNumber.matches("^[0-9]{10}$")) {
                return false;
            }
        }

        // Controlla se l'URL della foto è un URL valido e non supera gli 80 caratteri
        /*if (photoUrl != null && !photoUrl.isEmpty()){
            if (photoUrl.length() > 80 || !photoUrl.matches("^(ftp|http|https):\\/\\/[^ \"]+$")) {
                return false;
            }
        }

         */


        // Se tutti i controlli passano, restituisce true
        return true;
    }

    @Transactional
    public Socio createSocio(
            Utente utente,
            String email,
            String password,
            String phoneNumber,
            String photoUrl
    ){
        // Crea un nuovo socio
        Socio socio = new Socio();
        socio.setUtente(utente);
        socio.setId(utente.getId());
        socio.setEmail(email);
        socio.setPassword(password);
        socio.setTelefono(phoneNumber);
        socio.setUrlFoto(photoUrl);


        return socioRepository.save(socio);
    }


}
