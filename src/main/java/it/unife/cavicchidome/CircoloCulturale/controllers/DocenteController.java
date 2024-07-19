package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Docente;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.services.CorsoService;
import it.unife.cavicchidome.CircoloCulturale.services.SaggioService;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/docente")
public class DocenteController {
    private final SocioService socioService;
    private final CorsoService corsoService;
    private final SaggioService saggioService;

    @Autowired
    public DocenteController(SocioService socioService, CorsoService corsoService, SaggioService saggioService) {
        this.socioService = socioService;
        this.corsoService = corsoService;
        this.saggioService = saggioService;
    }

    @GetMapping("/corsi")
    public String segretarioViewCorsi(@RequestParam(name = "categoria") Optional<String> courseCategory,
                                      @RequestParam(name = "genere") Optional<String> courseGenre,
                                      @RequestParam(name = "livello") Optional<String> courseLevel,
                                      Model model,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        Optional<Socio> docente = socioService.setSocioFromCookie(request, response, model);
        if (docente.isPresent() && docente.get().getDocente() == null) {
            return "redirect:/";
        }

        model.addAttribute("categorie", corsoService.getCategorieActive());
        model.addAttribute("generi", corsoService.getGeneriActive());
        model.addAttribute("livelli", corsoService.getLivelliActive());
        model.addAttribute("corsi", corsoService.filterCorsiDocente(courseCategory, courseGenre, courseLevel, docente.get().getId()));
        return "corsi-docente";
    }

    @GetMapping("/saggi")
    public String viewSaggi(@RequestParam(name = "data") Optional<LocalDate> date,
                            Model model,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        Optional<Socio> docente = socioService.setSocioFromCookie(request, response, model);
        if (docente.isEmpty() || docente.get().getDocente() == null) {
            return "redirect:/";
        }

        model.addAttribute("saggi", saggioService.getSaggioAfterDateDocente(date, docente.get().getId()));
        return "saggi-docente";
    }
}
