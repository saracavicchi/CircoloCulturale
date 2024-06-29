package it.unife.cavicchidome.CircoloCulturale.controllers;

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
            model.addAttribute("socio", socioId);
            return "home";
        } else {
            return "redirect:/";
        }
    }
}
