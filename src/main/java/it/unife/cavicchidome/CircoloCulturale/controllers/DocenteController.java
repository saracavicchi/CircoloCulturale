package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.services.CorsoService;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/docente")
public class DocenteController {
    private final SocioService socioService;
    private final CorsoService corsoService;

    public DocenteController(SocioService socioService, CorsoService corsoService) {
        this.socioService = socioService;
        this.corsoService = corsoService;
    }

    @GetMapping("/corsi")
    public String segretarioViewCorsi(Model model,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        Optional<Socio> docente = socioService.setSocioFromCookie(request, response, model);
        if (docente.isPresent() && docente.get().getDocente() == null) {
            return "redirect:/";
        }

        model.addAttribute("corsi", corsoService.findCorsiByDocenteId(docente.get().getId()));
        return "corsi-docente";
    }
}
