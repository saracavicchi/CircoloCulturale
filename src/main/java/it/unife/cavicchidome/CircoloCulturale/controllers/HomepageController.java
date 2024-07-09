package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Sede;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.services.SaggioService;
import it.unife.cavicchidome.CircoloCulturale.services.SedeService;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public String index(HttpServletRequest request,
                        HttpServletResponse response,
                        Model model) {

        socioService.setSocioFromCookie(request, response, model);

        model.addAttribute("saggi", saggioService.getNextMonth());
        return "index";
    }

}
