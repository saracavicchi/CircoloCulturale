package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import it.unife.cavicchidome.CircoloCulturale.services.UtenteService;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.UtenteRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;


@Controller
public class socioProfileController {

    private UtenteService utenteService;
    private SocioService socioService;
    private UtenteRepository utenteRepository;
    private SocioRepository socioRepository;
    @Value("${file.socio.upload-dir}")
    private String uploadDir;

    socioProfileController(
            UtenteService utenteService,
            SocioService socioService,
            UtenteRepository utenteRepository,
            SocioRepository socioRepository
    ) {
        this.utenteService = utenteService;
        this.socioService = socioService;
        this.utenteRepository = utenteRepository;
        this.socioRepository = socioRepository;
    }


    @GetMapping("/socioProfile")
    public String showSocioProfile(
            Model model,
            @RequestParam("socioId") Integer socioId
    ) {
        // Recupera i dati del socio tramite il suo ID
        Optional<Socio> socioOpt = socioService.findById(socioId);
        if (!socioOpt.isPresent()) {
            return "redirect:/errorPage"; //TODO: gestire l'errore in modo più specifico
        }
        Socio socio = socioOpt.get();

        // Recupera i dati dell'utente associato al socio
        Utente utente = socio.getUtente();

        // Aggiunge i dati del socio e dell'utente al modello
        model.addAttribute("socio", socio);
        model.addAttribute("utente", utente);
        model.addAttribute("uploadDir", uploadDir);
        model.addAttribute("placeholderImage", "profilo.jpg");

        // Restituisce la vista socioProfile.jsp con i dati
        return "socioProfile";
    }

    @PostMapping("/socioProfile")
    public String changeSocioData(
            @RequestParam("socioId") Integer socioId,
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
            @RequestParam String phoneNumber,
            @RequestParam("photo") MultipartFile photo,
            RedirectAttributes redirectAttributes
    ) {
        System.out.println("socioId: " + socioId);
        Optional<Socio> socioOpt = socioService.findById(socioId);
        if (!socioOpt.isPresent()) {
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/socioProfile?socioId=" + socioId;
        }
        Socio socio = socioOpt.get();
        Utente utente = socio.getUtente();


        // Update the Utente entity with new values
        utente.setNome(name);
        utente.setCognome(surname);
        utente.setCf(cf);
        utente.setDataNascita(dob);
        utente.setLuogoNascita(birthplace);
        String indirizzo = String.join(", ", state, province, city, street, houseNumber);
        utente.setIndirizzo(indirizzo);

        // Validate the updated Utente and Socio information
        /*if (!utenteService.validateAndParseUtente(name, surname, cf, dob, birthplace, state, province, city, street, houseNumber) ||
                !socioService.validateAndParseSocio(email, socio.getPassword(), phoneNumber)) {
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/socioProfile?socioId=" + socioId;
        }*/ //TODO: Implementare i metodi di validazione con eccezione

        // Handle photo upload
        String filename = null;
        if (photo != null && !photo.isEmpty()) {
            if((filename = socioService.saveSocioProfilePicture(photo, cf))==null){
                redirectAttributes.addAttribute("failed", "true");
                return "redirect:/socioProfile?socioId=" + socioId;
            }
        }

        // Update the Socio entity with new values
        socio.setEmail(email);
        socio.setTelefono(phoneNumber);
        // Assuming Socio has setEmail and setPhoneNumber methods

        // Save the updated entities
        utenteRepository.save(utente);
        socioRepository.save(socio);

        return "redirect:/socioProfile?socioId=" + socioId;
    }

    @GetMapping("/modificaPassword")
    public String mostraModificaPassword(
            @RequestParam("socioId") Integer socioId,
            Model model
    ) {
        Optional<Socio> socioOpt = socioService.findById(socioId);
        if (!socioOpt.isPresent()) {
            return "redirect:/errorPage"; //TODO: Gestire l'errore in modo più specifico
        }
        Socio socio = socioOpt.get();

        // Aggiunge l'ID e la password del socio al modello
        model.addAttribute("socioId", socioId);
        model.addAttribute("passwordAttuale", socio.getPassword());

        // Restituisce la vista per la modifica della password
        return "modificaPassword";
    }



    @PostMapping("/modificaPassword")
    public String modificaPassword(
            @RequestParam("socioId") Integer socioId,
            @RequestParam("currentPassword") String passwordAttuale,
            @RequestParam("newPassword") String passwordNuova,
            HttpServletResponse response
    ) {
        Optional<Socio> socioOpt = socioService.findById(socioId);
        if (!socioOpt.isPresent()) {
            return "redirect:/errorPage"; // Gestire l'errore in modo più specifico
        }
        Socio socio = socioOpt.get();
        if (passwordAttuale.equals(passwordNuova)) {
            // Crea un cookie per segnalare che la nuova password è uguale a quella attuale
            Cookie cookie = new Cookie("alreadyPresent", "true");
            response.addCookie(cookie);
            return "redirect:/modificaPassword";
        }
        if(socioService.validatePassword(passwordNuova)){
            socio.setPassword(passwordNuova);
            socioRepository.save(socio);
            return "redirect:/socioProfile?socioId=" + socioId;
        }
        else{
            // Crea un cookie per segnalare che la nuova password non è valida
            Cookie cookie = new Cookie("failed", "true");
            response.addCookie(cookie);
            return "redirect:/modificaPassword";
        }

    }
}



