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

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/socio")
public class socioProfileController {

    private final SocioService socioService;

    socioProfileController(
            SocioService socioService) {
        this.socioService = socioService;
    }


    @GetMapping("/profile")
    public String showSocioProfile(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("socio-id") Optional<Integer> socioId
    ) {

        Optional<Socio> socioCookie = socioService.setSocioFromCookie(request, response, model);

        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }

        Socio socio;
        if (socioId.isPresent() && socioService.findSocioById(socioId.get()).isPresent()) {
            if (socioCookie.get().getSegretario() != null) {
                socio = socioService.findSocioById(socioId.get()).get();
            } else {
                return "redirect:/forbidden";
            }
        } else {
            socio = socioCookie.get();
        }

        // Aggiunge i dati del socio e dell'utente al modello
        if (socio.getUrlFoto() == null || socio.getUrlFoto().isEmpty()) {
            socio.setUrlFoto("profilo.jpg");
        }
        model.addAttribute("socio", socio);

        // Restituisce la vista socio-profile.jsp con i dati
        return "socio-profile";
    }

    @GetMapping("/saggi")
    public String getSocioSaggi(Model model,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        Optional<Socio> socioCookie = socioService.setSocioFromCookie(request, response, model);
        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("saggi", socioCookie.get().getSaggi());
        return "socio-saggi";
    }

    @GetMapping("/corsi")
    public String getSocioCorsi(Model model,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        Optional<Socio> socioCookie = socioService.setSocioFromCookie(request, response, model);
        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("corsi", socioCookie.get().getCorsi());
        return "socio-corsi";
    }

    @PostMapping("/profile") //TODO: RENDERE TRANSAZIONALE
    public String changeSocioData(
            @RequestParam("socio-id") Optional<Integer> socioId,
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
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<Socio> socioCookie = socioService.getSocioFromCookie(request, response);

        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }

        Socio socio;
        String redirectTo;
        if (socioId.isPresent()) {
            if (socioId.get().equals(socioCookie.get().getId())) {
                socio = socioCookie.get();
                redirectTo = "";
            } else if (socioService.findSocioById(socioId.get()).isPresent()) {
                if (socioCookie.get().getSegretario() != null) {
                    socio = socioService.findSocioById(socioId.get()).get();
                    redirectTo = "?socio-id=" + socioId.get();
                } else {
                    return "redirect:/forbidden";
                }
            } else {
                redirectAttributes.addAttribute("failed", "true");
                return "redirect:/socio/profile?socio-id=" + socioId.get();
            }
        } else {
            socio = socioCookie.get();
            redirectTo = "";
        }

        try {
            socioService.editSocioAndUtente(socio.getId(),
                    socio.getUtente().getId(),
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
            redirectAttributes.addAttribute("success", "true");
            return "redirect:/socio/profile" + redirectTo;
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/socio/profile" + redirectTo;
        }
    }

    @PostMapping("/modificaPassword")
    public String modificaPassword(
            @RequestParam("socio-id") Optional<Integer> socioId,
            @RequestParam("old-password") Optional<String> passwordAttuale,
            @RequestParam("new-password") String passwordNuova,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<Socio> socioCookie = socioService.getSocioFromCookie(request, response);

        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }

        Socio socio;
        String redirectTo;

        if (socioId.isPresent()) {
            if (socioId.get().equals(socioCookie.get().getId())) {
                socio = socioCookie.get();
                redirectTo = "";
            } else if (socioService.findSocioById(socioId.get()).isPresent()) {
                if (socioCookie.get().getSegretario() != null) {
                    socio = socioService.findSocioById(socioId.get()).get();
                    redirectTo = "?socio-id=" + socioId.get();
                } else {
                    return "redirect:/forbidden";
                }
            } else {
                redirectAttributes.addAttribute("passwordFailed", "true");
                return "redirect:/socio/profile?socio-id=" + socioId.get();
            }
        } else {
            socio = socioCookie.get();
            redirectTo = "";
        }

        try {
            socioService.updateSocioPassword(socio.getId(), passwordAttuale, passwordNuova);
            redirectAttributes.addAttribute("passwordSuccess", "true");
            return "redirect:/socio/profile" + redirectTo;
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("passwordFailed", "true");
            return "redirect:/socio/profile" + redirectTo;
        }
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



