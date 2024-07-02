package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.repositories.UtenteRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;


import org.springframework.format.annotation.DateTimeFormat;
import java.util.regex.Pattern;

@Controller
public class AuthController {

    private final SocioService socioService;
    SocioRepository socioRepository;
    UtenteRepository utenteRepository;

    AuthController(SocioRepository socioRepository, SocioService socioService, UtenteRepository utenteRepository){
        this.socioRepository = socioRepository;
        this.socioService = socioService;
        this.utenteRepository = utenteRepository;
    }

    @GetMapping("/login")
    public String viewLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String cf,
            @RequestParam String password,
            @RequestParam String redirectTo,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response
    ) {
        Optional<Integer> socioId = socioService.authenticate(cf, password);
        if (socioId.isPresent()) {
            Cookie socioCookie = new Cookie("socio-id", "" + socioId.get());
            response.addCookie(socioCookie);
            return "redirect:" + redirectTo;
        } else {
            redirectAttributes.addAttribute("authFailed", "true");
            return "redirect:" + redirectTo;
        }
    }

    @GetMapping("/logout")
    public String logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // Invalida la sessione
        //request.getSession().invalidate();

        // Rimuove il cookie di autenticazione
        Cookie socioCookie = new Cookie("socio-id", null);
        socioCookie.setMaxAge(0);
        response.addCookie(socioCookie);

        // Reindirizza l'utente alla pagina di login
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String viewSignup() {
        return "signup";
    }



    private boolean validateUserInfo(
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

    private boolean validateSocioInfo(
            String email,
            String password,
            String phoneNumber,
            String photoUrl
    ) {

        //MEGLIO LIBRERIA DI VALIDAZIONE
        // Controlla se l'email è un'email valida e non supera i 50 caratteri
        if (email == null || email.length() > 50 || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
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
        if (photoUrl != null && !photoUrl.isEmpty()){
            if (photoUrl.length() > 80 || !photoUrl.matches("^(ftp|http|https):\\/\\/[^ \"]+$")) {
                return false;
            }
        }


        // Se tutti i controlli passano, restituisce true
        return true;
    }

    private boolean validateRegistrationData(
            String name,
            String surname,
            String cf,
            LocalDate dob,
            String birthplace,
            String state,
            String province,
            String city,
            String street,
            String houseNumber,
            String email,
            String password,
            String phoneNumber,
            String photoUrl
    ) {
        //System.out.println(validateUserInfo(name, surname, cf, dob, birthplace, state, province, city, street, houseNumber));
        //System.out.println(validateSocioInfo(email, password, phoneNumber, photoUrl)); //Come controllo
        return validateUserInfo(name, surname, cf, dob, birthplace, state, province, city, street, houseNumber) &&
                validateSocioInfo(email, password, phoneNumber, photoUrl);
    }

    private Utente createUtente(
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

        return utente;
    }

    private Socio createSocio(
            Utente utente,
            String email,
            String password,
            String phoneNumber,
            String photoUrl
    ){
        // Crea un nuovo socio
        Socio socio = new Socio();
        socio.setId(utente.getId());
        socio.setUtente(utente);
        socio.setEmail(email);
        socio.setPassword(password);
        socio.setTelefono(phoneNumber);
        socio.setUrlFoto(photoUrl);

        return socio;
    }


    @PostMapping("/signup")
    public String register(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String cf,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
            @RequestParam String birthplace,
            @RequestParam String state,
            @RequestParam String province,
            @RequestParam String city,
            @RequestParam String street,
            @RequestParam String houseNumber,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phoneNumber,
            @RequestParam String photoUrl,
            RedirectAttributes redirectAttributes
    ) {
        // Valida i dati del form di registrazione
        if (!validateRegistrationData(name, surname, cf, dob, birthplace, state, province, city, street, houseNumber, email, password, phoneNumber, photoUrl)) {
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/signup";
        }

        // Registra utente se non presente nel Database
        if(utenteRepository.findByCf(cf) == null){
            Utente utente = createUtente(name, surname, cf, dob, birthplace, state, province, city, street, houseNumber);
            Socio socio = createSocio(utente, email, password, phoneNumber, photoUrl);
            utenteRepository.save(utente);
            socioRepository.save(socio);


        }
        else{
            redirectAttributes.addAttribute("failed", "true");
            redirectAttributes.addAttribute("alreadyPresent", "true");
            return "redirect:/signup";
        }
        // Reindirizza l'utente alla pagina di login
        return "redirect:/login";


    }



}
