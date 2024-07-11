package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.*;
import it.unife.cavicchidome.CircoloCulturale.repositories.CalendarioCorsoRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.PrenotazioneSalaRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.SalaRepository;
import it.unife.cavicchidome.CircoloCulturale.services.PrenotazioneSalaService;
import it.unife.cavicchidome.CircoloCulturale.services.SalaService;
import it.unife.cavicchidome.CircoloCulturale.services.SedeService;
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
    private final SedeService sedeService;
    private final SalaService salaService;
    private final CalendarioCorsoRepository calendarioCorsoRepository;
    private final PrenotazioneSalaRepository prenotazioneSalaRepository;
    private final SalaRepository salaRepository;

    public PrenotazioneController(SocioService socioService, PrenotazioneSalaService prenotazioneSalaService, SedeService sedeService, SalaService salaService, CalendarioCorsoRepository calendarioCorsoRepository, PrenotazioneSalaRepository prenotazioneSalaRepository, SalaRepository salaRepository) {
        this.socioService = socioService;
        this.prenotazioneSalaService = prenotazioneSalaService;
        this.sedeService = sedeService;
        this.salaService = salaService;
        this.calendarioCorsoRepository = calendarioCorsoRepository;
        this.prenotazioneSalaRepository = prenotazioneSalaRepository;
        this.salaRepository = salaRepository;
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

        // TODO: renderli transazionali
        model.addAttribute("sedi", salaRepository.findDistinctSedi());
        Sala sala;
        if (date.isPresent() && salaId.isPresent()) {
            if ((sala = salaService.findById(salaId.get()).orElse(null)) == null || !sala.getActive() || !sala.getPrenotabile()) {
                model.addAttribute("error", "Sala non trovata");
            } else {
                if (sedeService.sedeAvailableDate(sala.getIdSede().getId(), date.get()).orElse(null) == null) {
                    model.addAttribute("error", "Sala non disponibile nella data richiesta");
                } else {
                    model.addAttribute("prenotabile", true);
                    model.addAttribute("sala", sala);
                    model.addAttribute("date", date.get());
                    model.addAttribute("corsiPrenotati", calendarioCorsoRepository.findByGiornoSettimana(sala.getId(), Weekday.fromDayNumber(date.get().getDayOfWeek().getValue())));
                    model.addAttribute("prenotazioniEffettuate", prenotazioneSalaRepository.findBySalaAndData(sala.getId(), date.get()));
                }
            }
        }
        return "nuova-prenotazione";
    }
}
