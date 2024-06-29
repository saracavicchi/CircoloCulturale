package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    SocioRepository socioRepository;
    AuthService authService;

    AuthController(SocioRepository socioRepository, AuthService authService) {
        this.socioRepository = socioRepository;
        this.authService = authService;
    }

    @GetMapping("/login")
    public String viewLogin() {
        return "login";
    }

    // @Transactional
    @PostMapping("/login")
    public String login(
            @RequestParam String cf,
            @RequestParam String password,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response
    ) {
        Socio socio = authService.authenticate(cf, password);
        if (socio != null) {
            Cookie socioCookie = new Cookie("socio-id", "" + socio.getId());
            response.addCookie(socioCookie);
            return "redirect:/home";
        } else {
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/login";
        }
    }
}
