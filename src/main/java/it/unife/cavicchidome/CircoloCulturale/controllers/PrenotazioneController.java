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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
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

    @GetMapping("/socio/prenotazione")
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
                return "redirect:/socio/prenotazione";
            }
        } else {
            List<PrenotazioneSala> prenotazioni = prenotazioneSalaService.getPrenotazioneBySocio(socioCookie.get().getId());
            model.addAttribute("prenotazioni", prenotazioni);
            return "prenotazioni";
        }
    }

    @GetMapping("/socio/prenotazione/nuova")
    public String viewNuovaPrenotazione(@RequestParam(name = "data") Optional<LocalDate> date,
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
                    model.addAttribute("orari", sedeService.findOrarioSede(sala.getIdSede().getId(), Weekday.fromDayNumber(date.get().getDayOfWeek().getValue())));
                    model.addAttribute("date", date.get());
                    model.addAttribute("corsi", calendarioCorsoRepository.findByGiornoSettimana(sala.getId(), Weekday.fromDayNumber(date.get().getDayOfWeek().getValue())));
                    model.addAttribute("prenotazioni", prenotazioneSalaRepository.findBySalaAndData(sala.getId(), date.get()));
                }
            }
        }
        return "nuova-prenotazione";
    }

    @PostMapping("/socio/prenotazione/nuova")
    public String nuovaPrenotazione(@RequestParam(name = "descrizione") String description,
                                    @RequestParam(name = "orarioInizio") LocalTime startTime,
                                    @RequestParam(name = "orarioFine") LocalTime endTime,
                                    @RequestParam(name = "data") LocalDate date,
                                    @RequestParam(name = "sala") Integer salaId,
                                    RedirectAttributes redirectAttributes,
                                    Model model,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        Optional<Socio> socioCookie = socioService.setSocioFromCookie(request, response, model);
        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }

        Optional<Sala> sala = salaService.findById(salaId);
        if (sala.isEmpty() || !sala.get().getActive() || !sala.get().getPrenotabile()) {
            model.addAttribute("error", "Sala non trovata");
        } else {
            Optional<OrarioSede> orario = sedeService.findOrarioSede(sala.get().getIdSede().getId(), Weekday.fromDayNumber(date.getDayOfWeek().getValue()));
            if (orario.isEmpty() || orario.get().getId().getId() != orarioId) {
                model.addAttribute("error", "Orario non valido");
            } else {
                if (sedeService.sedeAvailableDate(sala.get().getIdSede().getId(), date).isEmpty()) {
                    model.addAttribute("error", "Sala non disponibile nella data richiesta");
                } else {
                    if (prenotazioneSalaService.createPrenotazione(sala.get(), socioCookie.get(), date, orario.get())) {
                        return "redirect:/socio/prenotazione";
                    } else {
                        model.addAttribute("error", "Errore nella creazione della prenotazione");
                    }
                }
            }
        }
        model.addAttribute("sedi", salaRepository.findDistinctSedi());
        model.addAttribute("sala", sala.get());
        model.addAttribute("orari", sedeService.findOrarioSede(sala.get().getIdSede().getId(), Weekday.fromDayNumber(date.getDayOfWeek().getValue())));
        model.addAttribute("date", date);
        model.addAttribute("corsi", calendarioCorsoRepository.findByGiornoSettimana(sala.get().getId(), Weekday.fromDayNumber(date.getDayOfWeek().getValue())));
        model.addAttribute("prenotazioni", prenotazioneSalaRepository.findBySalaAndData(sala.get().getId(), date));
        return "nuova-prenotazione";
    }
}
