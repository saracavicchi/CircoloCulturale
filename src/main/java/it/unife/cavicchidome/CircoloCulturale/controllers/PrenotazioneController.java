package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.PrenotazioneSala;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.services.PrenotazioneSalaService;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class PrenotazioneController {

    private final SocioService socioService;
    private final PrenotazioneSalaService prenotazioneSalaService;

    public PrenotazioneController(SocioService socioService, PrenotazioneSalaService prenotazioneSalaService) {
        this.socioService = socioService;
        this.prenotazioneSalaService = prenotazioneSalaService;
    }

    @GetMapping("/socio/prenotazioni")
    public String viewPrenotazioni(@RequestParam Optional<Integer> prenotazioneId,
                                   Model model,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        Optional<Socio> socioCookie = socioService.setSocioFromCookie(request, response, model);
        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }

        if (prenotazioneId.isPresent()) {
            Optional<PrenotazioneSala> prenotazione = prenotazioneSalaService.getPrenotazioneById(socioCookie.get().getId(), prenotazioneId.get());
            if (prenotazione.isPresent()) {
                model.addAttribute("prenotazione", prenotazione.get());
                return "prenotazione-info";
            } else {
                return "redirect:/socio/prenotazioni";
            }
        } else {
            List<PrenotazioneSala> prenotazioni = prenotazioneSalaService.getPrenotazioneBySocio(socioCookie.get().getId());
            model.addAttribute("prenotazioni", prenotazioni);
            return "prenotazioni";
        }
    }

    @GetMapping("/socio/prenotazioni/nuova")
    public String viewNuovaPrenotazione(@RequestParam(name = "date") Optional<LocalDate> date,
                                        @RequestParam(name = "sala") Optional<Integer> salaId,
                                        Model model,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        Optional<Socio> socioCookie = socioService.setSocioFromCookie(request, response, model);
        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }
        if (date.isPresent() && salaId.isPresent()) {

        }
        return "nuova-prenotazione";
    }
}
