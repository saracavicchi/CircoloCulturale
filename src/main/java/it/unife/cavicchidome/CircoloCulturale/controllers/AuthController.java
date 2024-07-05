package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.exceptions.EntityAlreadyPresentException;
import it.unife.cavicchidome.CircoloCulturale.exceptions.ValidationException;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import it.unife.cavicchidome.CircoloCulturale.services.TesseraService;
import it.unife.cavicchidome.CircoloCulturale.services.UtenteService;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.models.Tessera;
import it.unife.cavicchidome.CircoloCulturale.repositories.UtenteRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Controller
public class AuthController {

    private final SocioService socioService;
    private final UtenteService utenteService;
    SocioRepository socioRepository;
    UtenteRepository utenteRepository;
    TesseraService tesseraService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    AuthController(
            SocioRepository socioRepository,
            SocioService socioService,
            UtenteRepository utenteRepository,
            UtenteService utenteService,
            TesseraService tesseraService
    ){
        this.socioRepository = socioRepository;
        this.socioService = socioService;
        this.utenteRepository = utenteRepository;
        this.utenteService = utenteService;
        this.tesseraService = tesseraService;

    }

    @PostMapping("/login")
    public String login(
            @RequestParam String cf,
            @RequestParam String password,
            @RequestParam(defaultValue = "/") String redirectTo,
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


    @PostMapping("/logout")
    public String logout(
            @RequestParam(defaultValue = "/") String redirectTo,
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
        return "redirect:" + redirectTo;
    }

    @GetMapping("/signup")
    public String viewSignup() {
        return "signup";
    }


    //TO DO: Optional e dto
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
            @RequestParam("photo") MultipartFile photo,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Socio socio = socioService.newSocio(name, surname, cf, dob, birthplace, state, province, city, street, houseNumber, email, password, phoneNumber, Optional.empty(), photo);
            redirectAttributes.addAttribute("registered", "true");
            return "redirect:/login";
        } catch (ValidationException validExc) {
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/signup";
        } catch (EntityAlreadyPresentException entityExc) {
            redirectAttributes.addAttribute("failed", "true");
            redirectAttributes.addAttribute("alreadyPresent", "true");
            return "redirect:/signup";
        }
    }




}
