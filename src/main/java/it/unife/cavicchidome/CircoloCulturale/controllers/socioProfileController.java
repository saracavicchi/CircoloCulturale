package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.models.Segretario;
import it.unife.cavicchidome.CircoloCulturale.repositories.SegretarioRepository;
import it.unife.cavicchidome.CircoloCulturale.services.SegretarioService;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import it.unife.cavicchidome.CircoloCulturale.services.UtenteService;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.UtenteRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.beans.factory.annotation.Value;


@Controller
public class socioProfileController {

    private final SegretarioRepository segretarioRepository;
    private final UtenteService utenteService;
    private final SocioService socioService;
    private final UtenteRepository utenteRepository;
    private final SocioRepository socioRepository;
    private final SegretarioService segretarioService;
    @Value("${file.socio.upload-dir}")
    private String uploadDir;

    socioProfileController(
            UtenteService utenteService,
            SocioService socioService,
            UtenteRepository utenteRepository,
            SocioRepository socioRepository,
            SegretarioService segretarioService,
            SegretarioRepository segretarioRepository) {
        this.utenteService = utenteService;
        this.socioService = socioService;
        this.utenteRepository = utenteRepository;
        this.socioRepository = socioRepository;
        this.segretarioService = segretarioService;
        this.segretarioRepository = segretarioRepository;
    }


    @GetMapping("/socioProfile")
    public String showSocioProfile(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("socioId") /*Optional<Integer>*/ Integer socioId,
            @RequestParam("segretario") Optional<String> segretario,
            RedirectAttributes redirectAttributes
    ) {

//        Optional<Socio> socioCookie = socioService.setSocioFromCookie(request, response, model);
//        Optional<Socio> socio;
//        if (socioCookie.isEmpty() && socioId.isEmpty()) {
//            return "redirect:/";
//        } else if (socioId.isPresent()) {
//            socio = socioService.findSocioById(socioId.get());
//            if (socio.isEmpty()) {
//                return "redirect:/";
//            }
//        } else if (socioCookie.isPresent()) {
//            socio = socioCookie;
//        }

        // Recupera i dati del socio tramite il suo ID
        Optional<Socio> socioOpt = socioService.findSocioById(socioId);
        if (!socioOpt.isPresent() || socioOpt.get().getDeleted()==true) {
            return "redirect:/"; //TODO: gestire l'errore in modo più specifico
        }
        Socio socio = socioOpt.get();

        // Recupera i dati dell'utente associato al socio
        Utente utente = socio.getUtente();

        // Aggiunge i dati del socio e dell'utente al modello
        model.addAttribute("socio", socio);
        model.addAttribute("utente", utente);
        model.addAttribute("uploadDir", uploadDir);
        model.addAttribute("placeholderImage", "profilo.jpg");

        redirectAttributes.addAttribute("socioId", socioId);
        // Verifica se il Socio è anche un Segretario
        Optional<Segretario> segretarioOpt = segretarioService.findById(socioId); // o segretarioRepository.findById(socioId)
        if (segretarioOpt.isPresent() || segretario.isPresent()) {
            // Se il socio è anche un segretario, aggiungi attributo
            model.addAttribute("segretario", "true");
        }

        // Restituisce la vista socioProfile.jsp con i dati
        return "socioProfile";
    }

    @PostMapping("/socioProfile") //TODO: RENDERE TRANSAZIONALE
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
            @RequestParam("segretario") Optional<String> segretario,
            @RequestParam("photo") MultipartFile photo,

            RedirectAttributes redirectAttributes,
            Model model
    ) {
        redirectAttributes.addAttribute("socioId", socioId);
        if(segretario.isPresent()){
            model.addAttribute("segretario", "true");
        }
        Optional<Socio> socioOpt = socioService.findSocioById(socioId);
        if (!socioOpt.isPresent()) {
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/socioProfile" ;
        }
        Socio socio = socioOpt.get();
        Utente utente = socio.getUtente();

        try {
            socioService.editSocioAndUtente(socio.getId(),
                    utente.getId(),
                    Optional.of(name),
                    Optional.of(surname),
                    Optional.of(cf),
                    Optional.of(dob),
                    Optional.of(birthplace),
                    Optional.of(state),
                    Optional.of(province),
                    Optional.of(city),
                    Optional.of(street),
                    Optional.of(houseNumber),
                    Optional.of(email),
                    Optional.of(phoneNumber),
                    Optional.of(photo));
            return "redirect:/socioProfile?socioId=" + socioId;
        } catch (Exception exc) {
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/socioProfile?socioId=" + socioId;
        }
    }

    @GetMapping("/modificaPassword")
    public String mostraModificaPassword(
            @RequestParam("socioIdPassword") Integer socioId,
            @RequestParam("segretario") Optional<String> segretario,
            Model model
    ) {
        Optional<Socio> socioOpt = socioService.findSocioById(socioId);
        if (!socioOpt.isPresent()) {
            return "redirect:/errorPage"; //TODO: Gestire l'errore in modo più specifico
        }
        Socio socio = socioOpt.get();

        // Aggiunge l'ID e la password del socio al modello
        model.addAttribute("socioId", socioId);
        model.addAttribute("passwordAttuale", socio.getPassword());
        if(segretario.isPresent()){
            model.addAttribute("segretario", "true");
        }

        // Restituisce la vista per la modifica della password
        return "modificaPassword";
    }



    @PostMapping("/modificaPassword")
    public String modificaPassword(
            @RequestParam("socioId") Integer socioId,
            @RequestParam("currentPassword") Optional<String> passwordAttuale,
            @RequestParam("segretario") Optional<String> segretario,
            @RequestParam("newPassword") String passwordNuova,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        redirectAttributes.addAttribute("socioId", socioId);
        Optional<Socio> socioOpt = socioService.findSocioById(socioId);
        if (!socioOpt.isPresent()) {
            return "redirect:/errorPage"; // Gestire l'errore in modo più specifico
        }
        Socio socio = socioOpt.get();
        if(!segretario.isPresent()){
            if (passwordAttuale.equals(passwordNuova)) {
                //  segnalare che la nuova password è uguale a quella attuale
                redirectAttributes.addAttribute("alreadyPresent", "true");
                return "redirect:/modificaPassword";
            }

        }
        if(socioService.validatePassword(passwordNuova)){
            boolean updateSuccess = socioService.updateSocioPassword(socioId, passwordNuova);
            if (updateSuccess) {
                if(segretario.isPresent()){
                    model.addAttribute("segretario", "true");
                }
                return "redirect:/socioProfile";
            } else {
                //segnalare che la modifica della password non è andata a buon fine
                redirectAttributes.addAttribute("failed", "true");
                return "redirect:/modificaPassword";
            }
        } else {
            //segnalare che la nuova password non è valida
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/modificaPassword";
        }

    }

    @PostMapping("/segretarioModificaSocio")
    public String modificaSocioInfo(
            @RequestParam("socioId") Integer socioId,
            @RequestParam("cfSocio") String cf,
            @RequestParam("password") String password,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        final String PASSWORD = "PASSWORD"; // Assumed constant password value

        /*if (!segretarioService.validateCommonPassword(password)) {
            redirectAttributes.addAttribute("socioId", socioId);
            redirectAttributes.addAttribute("failSocioMod", "true");
            return "redirect:/socioProfile";
        }*/
        redirectAttributes.addAttribute("segretario", "true");


        Optional<Utente> utenteOpt = utenteService.findByCf(cf);
        if (utenteOpt.isPresent()) {
            Optional<Socio> socioOpt = socioService.findSocioById(utenteOpt.get().getId());
            if (socioOpt.isPresent()) {
                redirectAttributes.addAttribute("socioId", socioOpt.get().getId());
                return "redirect:/socioProfile";
            }
        }
        redirectAttributes.addAttribute("socioId", socioId);
        redirectAttributes.addAttribute("failSocioMod", "true");
        return "redirect:/socioProfile";

    }

    @PostMapping("/eliminaSocio") //TODO: GESTIRE TRANSAZIONALITà
    public String eliminaSocio(
            @RequestParam("socioIdElimina") Integer socioId,
            @CookieValue("socio-id") Integer socioIdCookie,
            @RequestParam("segretario") Optional<String> segretario,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Socio> socioOpt = socioService.findSocioById(socioId);
        if (socioOpt.isPresent()) {
            socioService.deleteSocioAndUser(socioId);
        } else {
            redirectAttributes.addAttribute("fail", "true");
            redirectAttributes.addAttribute("socioId", socioId);
            return "redirect:/socioProfile";
        }

        if (segretario.isPresent() && segretario.get().equals("true") && !socioId.equals(socioIdCookie)) {
            // Se l'utente è un segretario, reindirizza al profilo del segretario
            return "redirect:/socioProfile?socioId=" + socioIdCookie;
        } else {
            // Se l'utente non è un segretario o è un segretario che sta eliminando il suo profilo, reindirizza alla homepage
            Cookie socioCookie = new Cookie("socio-id", null);
            socioCookie.setMaxAge(0);// Invalida il cookie
            response.addCookie(socioCookie);
            return "redirect:/";
        }
    }
}



