package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.entity.Sede;
import it.unife.cavicchidome.CircoloCulturale.models.entity.Socio;
import it.unife.cavicchidome.CircoloCulturale.services.SaggioService;
import it.unife.cavicchidome.CircoloCulturale.services.SedeService;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class HomepageController {

    SocioService socioService;
    SedeService sedeService;
    SaggioService saggioService;

    HomepageController(SocioService socioService,
                       SedeService sedeService,
                       SaggioService saggioService) {
        this.socioService = socioService;
        this.sedeService = sedeService;
        this.saggioService = saggioService;
    }

    @GetMapping("/")
    public String index(@CookieValue(name = "socio-id", required = false) Optional<Integer> socioId, Model model) {
        if (socioId.isPresent()) {
            Optional<Socio> socio = socioService.findSocioById(socioId.get());
            if (socio.isPresent()) {
                model.addAttribute("socio", socio.get());
            }
        }
        model.addAttribute("saggi", saggioService.getNextMonth());
        model.addAttribute("redirectTo", "/");
        return "index";
    }

    @GetMapping("/sedi")
    public String sedi(@CookieValue(name = "socio-id", required = false) Optional<Integer> socioId,
                       @RequestParam(name = "id") Optional<Integer> sedeId,
                       Model model) {
        if (socioId.isPresent()) {
            Optional<Socio> socio = socioService.findSocioById(socioId.get());
            if (socio.isPresent()) {
                model.addAttribute("socio", socio.get());
            }
        }
        if (sedeId.isPresent()) {
            Optional<Sede> sede = sedeService.findSedeById(sedeId.get());
            if (sede.isPresent()) {
                model.addAttribute("sede", sede.get());
            }
            return "sede-info";
        } else {
            model.addAttribute("sedi", sedeService.getAll());
            model.addAttribute("redirectTo", "/sedi");
            return "sedi";
        }
    }

    @GetMapping("/saggi")
    public String saggi() {
        return "saggi";
    }

    @GetMapping("/home")
    public String viewHome(@CookieValue(name = "socio-id", required = false) Optional<Integer> socioId, Model model) {
        if (socioId.isPresent()) {
            Optional<Socio> socio = socioService.findSocioById(socioId.get());
            if (socio.isPresent()) {
                model.addAttribute("socio", socio.get());
            }
            return "home";
        } else {
            return "redirect:/";
        }
    }
}
