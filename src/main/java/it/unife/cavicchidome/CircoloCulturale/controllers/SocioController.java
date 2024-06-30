package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class SocioController {

    SocioRepository socioRepository;

    SocioController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @GetMapping("/home")
    public String viewHome(@CookieValue(name = "socio-id", required = false) Integer socioId, Model model) {
        if (socioId != null) {
            model.addAttribute("socio_id", socioId);
            Socio socio = socioRepository.getReferenceById(socioId);
            model.addAttribute("name", socio.getUtente().getNome());
            model.addAttribute("surname", socio.getUtente().getCognome());
            return "home";
        } else {
            return "redirect:/";
        }
    }
}
