package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    private final SocioService socioService;
    SocioRepository socioRepository;

    AuthController(SocioRepository socioRepository, SocioService socioService) {
        this.socioRepository = socioRepository;
        this.socioService = socioService;
    }

    @GetMapping("/login")
    public String viewLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String cf,
            @RequestParam String password,
            @RequestParam String redirectTo,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response,
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
}
