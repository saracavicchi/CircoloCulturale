package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Docente;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.services.CorsoService;
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

    @Autowired
    public DocenteController(SocioService socioService, CorsoService corsoService) {
        this.socioService = socioService;
        this.corsoService = corsoService;
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

        model.addAttribute("categorie", corsoService.getCategorie());
        model.addAttribute("generi", corsoService.getGeneri());
        model.addAttribute("livelli", corsoService.getLivelli());
        model.addAttribute("corsi", corsoService.filterCorsiDocente(courseCategory, courseGenre, courseLevel, docente.get().getId()));
        return "corsi-docente";
    }

    /*@GetMapping("/saggi")
    public String viewSaggi(@RequestParam(name = "data") Optional<LocalDate> date,
                            @RequestParam(name = "deleted") Optional<Boolean> deleted,
                            Model model,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        Optional<Socio> segretario = socioService.setSocioFromCookie(request, response, model);
        if (segretario.isEmpty() || segretario.get().getSegretario() == null) {
            return "redirect:/";
        }

        model.addAttribute("saggi", saggioService.getSaggioAfterDateDeleted(date, deleted));
        return "saggi-segretario";
    }*/
}
