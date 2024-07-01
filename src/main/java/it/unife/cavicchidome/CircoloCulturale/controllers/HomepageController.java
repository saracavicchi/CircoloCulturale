package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.repositories.SedeRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import it.unife.cavicchidome.CircoloCulturale.services.SedeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

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
    public String index(@CookieValue(name = "socio-id", required = false) Optional<Integer> socioId, Model model) {
        if (socioId.isPresent()) {
            Optional<Socio> socio = socioRepository.findById(socioId.get());
            if (socio.isPresent()) {
                model.addAttribute("socio", socio.get());
            }
        }
        model.addAttribute("sedi", sedeService.getAll());
        return "index";
    }

    @GetMapping("/home")
    public String viewHome(@CookieValue(name = "socio-id", required = false) Optional<Integer> socioId, Model model) {
        if (socioId.isPresent()) {
            Socio socio = socioRepository.getReferenceById(socioId.get());
            model.addAttribute("socio", socio);
            return "home";
        } else {
            return "redirect:/";
        }
    }
}
