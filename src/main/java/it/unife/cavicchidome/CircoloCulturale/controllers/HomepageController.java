package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.repositories.SedeRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import it.unife.cavicchidome.CircoloCulturale.services.SedeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomepageController {

    SocioRepository socioRepository;
    SedeService sedeService;

    HomepageController(SocioRepository socioRepository,
                       SedeService sedeService) {
        this.socioRepository = socioRepository;
        this.sedeService = sedeService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("sedi", sedeService.getAll());
        return "index";
    }

    @GetMapping("/home")
    public String viewHome(@CookieValue(name = "socio-id", required = false) Integer socioId, Model model) {
        if (socioId != null) {
            Socio socio = socioRepository.getReferenceById(socioId);
            model.addAttribute("socio", socio);
            return "home";
        } else {
            return "redirect:/";
        }
    }
}
