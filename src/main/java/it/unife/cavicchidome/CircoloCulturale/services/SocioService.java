package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

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

    public void getSocioFromCookie(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Model model) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("socio-id")) {
                Optional<Socio> socio = Optional.empty();
                try {
                    socio = socioRepository.findById(Integer.parseInt(cookie.getValue()));
                } catch (NumberFormatException nfexc) {
                    System.err.println(nfexc.getMessage());
                }
                if (socio.isPresent()) {
                    model.addAttribute("socio", socio.get());
                } else {
                    Cookie invalidateCookie = new Cookie("socio-id", null);
                    invalidateCookie.setMaxAge(0);
                    response.addCookie(invalidateCookie);
                }
            }
        }
    }

    @Transactional
    public Optional<Socio> findSocioById(int id) {
        return socioRepository.findById(id);
    }

    public boolean validateSocioInfo(
            String email,
            String password,
            String phoneNumber
            //String photoUrl
    ) {

        if (!validatePassword(password) || !validateEmail(email) || !validatePhoneNumber(phoneNumber)) {
            return false;
        }

        // Se tutti i controlli passano, restituisce true
        return true;
    }

    public boolean validatePassword(String password) {
        // Controlla se la password ha almeno 8 caratteri, almeno una lettera maiuscola, una lettera minuscola, un numero e non supera i 50 caratteri
        return password != null && password.length() <= 50 && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,50}$");
    }

    public boolean validateEmail(String email) {
        // Crea un'istanza di EmailValidator
        EmailValidator emailValidator = EmailValidator.getInstance();
        return emailValidator.isValid(email);
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        // Controlla se il numero di telefono contiene solo numeri e ha esattamente 10 cifre
        return phoneNumber != null && phoneNumber.matches("^[0-9]{10}$");
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

    public void sendEmail(Socio socio) {
        /*
        final String username = "indirizzomail";
        final String password = "app password"; // replace with your password

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
            message.setFrom(new InternetAddress("indirizzomailmittente"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(socio.getEmail())
            );
            message.setSubject("Benvenuto al Circolo Culturale");
            message.setText("Ciao " + socio.getUtente().getNome() + ","
                    + "\n\n Benvenuto al Circolo Culturale! Il tuo codice tessera è: " + socio.getTessera().getId());

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

         */
    }

    @Transactional
    public Optional<Socio> findById(Integer socioId) {
        return socioRepository.findById(socioId);
    }

    public String createPhotoName(MultipartFile photo, String cf) {

        // Ottieni l'estensione del file
        String originalFilename = photo.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // Crea il nome del file utilizzando il codice fiscale del socio e l'estensione del file
        return cf + extension;


    }


}
