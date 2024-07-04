package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.exceptions.EntityAlreadyPresentException;
import it.unife.cavicchidome.CircoloCulturale.exceptions.ValidationException;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SocioService {

    private final UtenteService utenteService;
    SocioRepository socioRepository;

    @Value("${file.upload-dir}")
    String uploadDir;

    SocioService(SocioRepository socioRepository, UtenteService utenteService) {
        this.socioRepository = socioRepository;
        this.utenteService = utenteService;
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

    public void setSocioFromCookie(HttpServletRequest request,
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

    Socio validateAndParseSocio(String email,
                                String password,
                                String phoneNumber) throws ValidationException {
        // Crea un'istanza di EmailValidator
        EmailValidator emailValidator = EmailValidator.getInstance();

        // Controlla se l'email è un'email valida e non supera i 50 caratteri
        if (email == null || email.length() > 50 || !emailValidator.isValid(email)) {
            throw new ValidationException("Email non valida");
        }

        // Controlla se la password ha almeno 8 caratteri, almeno una lettera maiuscola, una lettera minuscola, un numero e non supera i 50 caratteri
        if (password == null || password.length() > 50 || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,50}$")) {
            throw new ValidationException("Password non valida");
        }

        // Controlla se il numero di telefono contiene solo numeri e ha esattamente 10 cifre
        if (phoneNumber != null && !phoneNumber.isEmpty()){
            if( !phoneNumber.matches("^[0-9]{10}$")) {
                throw new ValidationException("Numero di telefono non valido");
            }
        }

        // Controlla se l'URL della foto è un URL valido e non supera gli 80 caratteri
        /*if (photoUrl != null && !photoUrl.isEmpty()){
            if (photoUrl.length() > 80 || !photoUrl.matches("^(ftp|http|https):\\/\\/[^ \"]+$")) {
                return false;
            }
        }

         */

        return new Socio(email, password, phoneNumber);
    }

    String saveSocioProfilePicture (MultipartFile picture, String cf) {
        String originalFilename = picture.getOriginalFilename();
        if (originalFilename == null) {
            return null;
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        String filename = cf + extension;

        try {
            Path picturePath = Paths.get(uploadDir, filename);
            picture.transferTo(picturePath);
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            return null;
        }

        if (picture != null && !picture.isEmpty()) {
            // Salva la foto del socio
            //String photoUrl = fileService.saveFile(picture);
            //return photoUrl;
            return null;
        }
    }

    @Transactional
    public Socio newSocio(
            String name,
            String surname,
            String cf,
            LocalDate dob,
            String pob,
            String country,
            String province,
            String city,
            String street,
            String houseNumber,
            String email,
            String password,
            String phone,
            String photoUrl
    ) throws ValidationException, EntityAlreadyPresentException {

        Utente utente;
        try {
            utente = utenteService.newUtente(name, surname, cf, dob, pob, country, province, city, street, houseNumber);
        } catch (EntityAlreadyPresentException exc) {
            utente = exc.getEntity();
        }

        if (utente.getSocio() != null) {
            throw new EntityAlreadyPresentException(utente.getSocio());
        }

        Socio socio = validateAndParseSocio(email, password, phone);
        socio.setDeleted(false);
        socio.setUtente(utente);

        return socioRepository.save(socio);
    }

    public void sendEmail(Socio socio) {
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
    }


}
