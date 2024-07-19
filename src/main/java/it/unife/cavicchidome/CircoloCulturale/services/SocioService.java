package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.exceptions.EntityAlreadyPresentException;
import it.unife.cavicchidome.CircoloCulturale.exceptions.ValidationException;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.models.Tessera;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.repositories.DocenteRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import it.unife.cavicchidome.CircoloCulturale.repositories.UtenteRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.UtenteRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.misc.LogManager;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
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
    private final TesseraService tesseraService;
    private final SocioRepository socioRepository;
    private final UtenteRepository utenteRepository;
    private final DocenteRepository docenteRepository;

    @Value("${file.socio.upload-dir}")
    String uploadDir;

    SocioService(SocioRepository socioRepository, UtenteService utenteService, TesseraService tesseraService, UtenteRepository utenteRepository, DocenteRepository docenteRepository) {
        this.socioRepository = socioRepository;
        this.utenteService = utenteService;
        this.tesseraService = tesseraService;
        this.utenteRepository = utenteRepository;
        this.docenteRepository = docenteRepository;
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

    public Optional<Socio> setSocioFromCookie(HttpServletRequest request,
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
                    model.addAttribute("socioHeader", socio.get());
                    return socio;
                } else {
                    Cookie invalidateCookie = new Cookie("socio-id", null);
                    invalidateCookie.setMaxAge(0);
                    response.addCookie(invalidateCookie);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Socio> getSocioFromCookie(HttpServletRequest request,
                                                HttpServletResponse response) {
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
                    return socio;
                } else {
                    Cookie invalidateCookie = new Cookie("socio-id", null);
                    invalidateCookie.setMaxAge(0);
                    response.addCookie(invalidateCookie);
                }
            }
        }
        return Optional.empty();
    }

    @Transactional
    public void confirmTessera(Integer socioId, Boolean confirmed) {
        Socio socio = socioRepository.getReferenceById(socioId);
        socio.getTessera().setStatoPagamento(confirmed ? 'c' : 'p');
        socioRepository.save(socio);
    }

    @Transactional
    public Optional<Socio> findSocioById(int id) {
        return socioRepository.findById(id);
    }

    @Transactional
    public Optional<Socio> findSocioByIdAll(int id) {
        return socioRepository.findByIdAll(id);
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
            Optional<BigDecimal> price,
            MultipartFile profilePicture
    ) throws ValidationException, EntityAlreadyPresentException {

        Utente utente;
        try {
            utente = utenteService.newUtente(name, surname, cf, dob, pob, country, province, city, street, houseNumber);
        } catch (EntityAlreadyPresentException exc) {
            utente = exc.getEntity();
        }
        Socio socio;
        if (utente.getSocio() != null ) {
            if(!utente.getSocio().getDeleted()){
                throw new EntityAlreadyPresentException(utente.getSocio());
            }else{
                socio = utente.getSocio();
                socio.setUtente(utente);
                if(!validateEmail(email) || !validatePhoneNumber(phone) || !validatePassword(Optional.empty(),password)){
                    throw new ValidationException("validazione errata");
                }
                socio.setEmail(email);
                socio.setPassword(password);
                socio.setTelefono(phone);
            }

        }else{
            socio = validateAndParseSocio(email, password, phone);
            socio.setUtente(utente);
            Tessera tessera = tesseraService.newTessera(socio, price);
            socio.setTessera(tessera);
        }

        socio.setDeleted(false);
        String profilePictureFilename = saveSocioProfilePicture(profilePicture, utente.getCf());
        socio.setUrlFoto(profilePictureFilename);



        sendEmail(socio);

        return socioRepository.save(socio);
    }

    @Transactional
    public Socio editSocioAndUtente(Integer socioId,
                                    Integer utenteId,
                                    Optional<String> name,
                                    Optional<String> surname,
                                    Optional<String> cf,
                                    Optional<LocalDate> dob,
                                    Optional<String> birthplace,
                                    Optional<String> country,
                                    Optional<String> province,
                                    Optional<String> city,
                                    Optional<String> street,
                                    Optional<String> houseNumber,
                                    Optional<String> email,
                                    Optional<String> phoneNumber,
                                    Optional<MultipartFile> profilePicture) throws ValidationException, EntityNotFoundException {
        utenteService.editUtente(utenteId, name, surname, cf, dob, birthplace, country, province, city, street, houseNumber);
        return editSocio(socioId, email, phoneNumber, profilePicture);
    }

    @Transactional
    public Socio editSocio(Integer socioId,
                           Optional<String> email,
                           Optional<String> phoneNumber,
                           Optional<MultipartFile> profilePicture) throws ValidationException, EntityNotFoundException {
        Socio socio = socioRepository.getReferenceById(socioId);
        email.ifPresent(socio::setEmail);
        phoneNumber.ifPresent(socio::setTelefono);
        profilePicture.ifPresent(picture -> {
            String filename = saveSocioProfilePicture(picture, socio.getUtente().getCf());
            socio.setUrlFoto(filename);
        });

        validateSocio(socio);
        return socioRepository.save(socio);
    }

    Socio validateAndParseSocio(String email,
                                String password,
                                String phoneNumber) throws ValidationException {

        // Controlla se l'email è un'email valida e non supera i 50 caratteri
        if (!validateEmail(email)) {
            throw new ValidationException("Email non valida");
        }

        // Controlla se la password ha almeno 8 caratteri, almeno una lettera maiuscola, una lettera minuscola, un numero e non supera i 50 caratteri
        if (!validatePassword(Optional.empty(), password)) {
            throw new ValidationException("Password non valida");
        }

        // Controlla se il numero di telefono contiene solo numeri e ha esattamente 10 cifre
        if (!validatePhoneNumber(phoneNumber)) {
            throw new ValidationException("Numero di telefono non valido");

        }
        return new Socio(email, password, phoneNumber);
    }


    public boolean validatePassword(Optional<String> oldPassword, String newPassword) {
        // Controlla se la password ha almeno 8 caratteri, almeno una lettera maiuscola, una lettera minuscola, un numero e non supera i 50 caratteri
        return newPassword != null &&
                newPassword.length() <= 50 &&
                newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,50}$") &&
                (oldPassword.isPresent() ? !oldPassword.get().equals(newPassword) : true);
    }

    public boolean validateEmail(String email) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        // Controlla se l'email è un'email valida e non supera i 50 caratteri
        if (email == null || email.length() > 50 || !emailValidator.isValid(email)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty()){
            if( !phoneNumber.matches("^[0-9]{10}$")) {
                return false;
            }
        }
        return true;
    }

    public Socio validateSocio(Socio socio) throws ValidationException {
        return validateAndParseSocio(socio.getEmail(), socio.getPassword(), socio.getTelefono());
    }

    String saveSocioProfilePicture(MultipartFile picture, String cf) {
        if (picture == null || picture.isEmpty()) {
            return null;
        }

        String originalFilename = picture.getOriginalFilename();
        if (originalFilename == null) {
            return null;
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = cf + extension;
        //String filename = filenameConSpazi.replace(" ", "");

        try {
            // Percorso relativo alla directory resources/static del progetto
            String relativePath = "static/images/socioProfilePhotos";
            // Costruisce il percorso completo utilizzando il percorso del progetto
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "src/main/resources", relativePath);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path picturePath = uploadPath.resolve(filename);
            System.out.println("Tentativo di salvataggio in: " + picturePath.toAbsolutePath());

            picture.transferTo(picturePath);

            if (Files.exists(picturePath)) {
                System.out.println("File salvato correttamente in" + picturePath.toAbsolutePath());
            } else {
                System.out.println("Il file non è stato salvato.");
                return null;
            }

            // Restituisce il percorso relativo per l'accesso via URL
            //return Paths.get(relativePath, filename).toString().replace("\\", "/");
            return filename;
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
            exc.printStackTrace();
            return null;
        }
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
                    + "\n\nBenvenuto al Circolo Culturale! Il tuo codice tessera è: " + socio.getTessera().getId());

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteSocioAndUser(Integer socioId, Optional<Boolean> delete){
        Socio deleteSocio = socioRepository.getReferenceById(socioId);
        deleteSocio.setDeleted(delete.orElse(true));
        if(deleteSocio.getUrlFoto() != null){
            try {
                deletePhoto(socioId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        deleteSocio.setCorsi(null);
        if(deleteSocio.getDocente() != null){
            deleteSocio.getDocente().setStipendio(BigDecimal.ZERO);
            deleteSocio.getDocente().setActive(false);
            deleteSocio.getDocente().setCorsi(null);
            docenteRepository.save(deleteSocio.getDocente());
            deleteSocio.setDocente(null);
        }
        //deleteSocio.getUtente().setDeleted(delete.orElse(true));
        socioRepository.save(deleteSocio);
        utenteRepository.save(deleteSocio.getUtente());
    }
    @Transactional
    public void updateSocioPassword(Integer socioId, Optional<String> oldPassword, String newPassword) throws EntityNotFoundException, ValidationException {
        Socio socio = socioRepository.getReferenceById(socioId);

        if (validatePassword(oldPassword, newPassword)) {
            socio.setPassword(newPassword);
            socioRepository.save(socio);
        } else {
            throw new ValidationException("Password non valida");
        }
    }

    @Transactional
    public List<Socio> findSocioCognomeInitial(Character cognome, boolean deleted) {
        return socioRepository.findSociByCognomeStartingWithAndDeleted(cognome, deleted);
    }

    @Transactional
    public List<Character> getSociInitials() {
        return socioRepository.findDistinctInitials();
    }

    @Transactional
    public List<Object[]> findSociNotSegretari() {
        return socioRepository.findSociNotSegretari();
    }

    @Transactional
    public List<Object[]> findSociNotDocentiAndNotSegretariByIdCorso(Integer idCorso) {
        return socioRepository.findSociNotDocentiAndNotSegretariByIdCorso(idCorso);
    }

    @Transactional(readOnly = true)
    public boolean existsSocioWithCf(String cf) {
        Optional<Utente> utente = utenteRepository.findByCf(cf);
        if (!utente.isPresent()) {
            return false;
        }
        return socioRepository.findById(utente.get().getId()).isPresent();
    }

    @Transactional
    public List<Object[]> findSociPossibiliSegretari() {
        return socioRepository.findSociPossibiliSegretari();
    }

    @Transactional
    public void deletePhoto(Integer socioId) throws Exception {
        Optional<Socio> socioOptional = socioRepository.findById(socioId);//solo active
        if (socioOptional.isPresent()) {
            Socio socio = socioOptional.get();
            String photoFilename = socio.getUrlFoto();
            socio.setUrlFoto(null);
            if (photoFilename != null && !photoFilename.isEmpty()) {
                Path fileStorageLocation = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/images/socioProfilePhotos/"+photoFilename);
                System.out.println("Tentativo di eliminazione in: " + fileStorageLocation);
                Files.deleteIfExists(fileStorageLocation);
                System.out.println("File eliminato correttamente in" + fileStorageLocation);
            }
            else{
                System.out.println("File non eliminato");
            }
        } else {
            throw new Exception("Socio not found with ID: " + socioId);
        }
    }





}
