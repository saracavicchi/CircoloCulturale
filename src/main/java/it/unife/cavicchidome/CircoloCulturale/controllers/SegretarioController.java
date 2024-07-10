package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/segretario")
public class SegretarioController {

    private final SocioService socioService;

    public SegretarioController(SocioService socioService) {
        this.socioService = socioService;
    }

    @GetMapping("/soci")
    public String viewSoci (@RequestParam(name = "initial") Optional<Character> initial,
                            @RequestParam(name = "deleted") Optional<Boolean> showDeleted,
                            Model model,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        Optional<Socio> socio = socioService.setSocioFromCookie(request, response, model);
        if (socio.isEmpty() || socio.get().getSegretario() == null) {
            return "redirect:/";
        }

        List<Character> initials = socioService.getSociInitials();
        if (initial.isEmpty() || !initials.contains(initial.get())) {
            return "redirect:/segretario/soci?initial=" + initials.getFirst();
        }
        List<Socio> soci = socioService.findSocioCognomeInitial(initial.get(), showDeleted.orElse(false));
        model.addAttribute("soci", soci);
        model.addAttribute("initials", initials);
        return "soci";
    }

    @GetMapping("/nuovoSocio")
    public String nuovoSocio(Model model, HttpServletRequest request, HttpServletResponse response) {
        Optional<Socio> socio = socioService.setSocioFromCookie(request, response, model);
        if (socio.isEmpty() || socio.get().getSegretario() == null) {
            return "redirect:/";
        }
        return "nuovo-socio";
    }
}
