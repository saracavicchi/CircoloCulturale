package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.exceptions.ValidationException;
import it.unife.cavicchidome.CircoloCulturale.models.*;
import it.unife.cavicchidome.CircoloCulturale.repositories.CalendarioCorsoRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.PrenotazioneSalaRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.SalaRepository;
import it.unife.cavicchidome.CircoloCulturale.services.PrenotazioneSalaService;
import it.unife.cavicchidome.CircoloCulturale.services.SalaService;
import it.unife.cavicchidome.CircoloCulturale.services.SedeService;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
    public String viewPrenotazioniSocio(@RequestParam(name = "id") Optional<Integer> prenotazioneId,
                                        @RequestParam(name = "data") Optional<LocalDate> showAfter,
                                        Model model,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        Optional<Socio> socioCookie = socioService.setSocioFromCookie(request, response, model);
        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }

        if (prenotazioneId.isPresent()) {
            Optional<PrenotazioneSala> prenotazione = prenotazioneSalaService.getPrenotazioneById(socioCookie.get().getId(), prenotazioneId.get());//TODO: Serve controllo su prenotazioni/soci attivi?
            if (prenotazione.isPresent()) {
                model.addAttribute("prenotazione", prenotazione.get());
                return "prenotazione-info";
            } else {
                return "redirect:/socio/prenotazioni";
            }
        } else {
            List<PrenotazioneSala> prenotazioni = prenotazioneSalaService.getPrenotazioneBySocio(socioCookie.get().getId(), showAfter);
            model.addAttribute("prenotazioni", prenotazioni);
            return "prenotazioni";
        }
    }

    @GetMapping("/socio/prenotazioni/nuova")
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
        model.addAttribute("sale", salaRepository.findAllPrenotabili());
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

    @PostMapping("/socio/prenotazioni/nuova")
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

        try {
            PrenotazioneSala prenotazione = prenotazioneSalaService.newPrenotazione(description, startTime, endTime, date, salaId, socioCookie.get().getId());
            redirectAttributes.addAttribute("success", "true");
            return "redirect:/socio/prenotazioni?id=" + prenotazione.getId();
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/socio/prenotazioni/nuova?data=" + date + "&sala=" + salaId;
        }
    }

    @GetMapping("/segretario/prenotazioni")
    public String viewPrenotazioniSegretario(@RequestParam(name = "id") Optional<Integer> prenotazioneId,
                                            @RequestParam(name = "data") Optional<LocalDate> showAfter,
                                            @RequestParam(name = "sala") Optional<Integer> salaId,
                                            @RequestParam(name = "deleted") Optional<Boolean> deleted,
                                            Model model,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        Optional<Socio> socioCookie = socioService.setSocioFromCookie(request, response, model);
        if (socioCookie.isEmpty() || socioCookie.get().getSegretario() == null || !socioCookie.get().getSegretario().getActive()) {
            return "redirect:/";
        }

        if (prenotazioneId.isPresent()) {
            Optional<PrenotazioneSala> prenotazione = prenotazioneSalaService.getPrenotazioneById(socioCookie.get().getId(), prenotazioneId.get());
            if (prenotazione.isPresent()) {
                model.addAttribute("prenotazione", prenotazione.get());
                return "prenotazione-info";
            } else {
                return "redirect:/segretario/prenotazioni";
            }
        } else {
            model.addAttribute("segretario", true);
            model.addAttribute("sedi", salaService.findDistinctSedi());
            model.addAttribute("sale", salaService.findAllPrenotabili());
            List<PrenotazioneSala> prenotazioni = prenotazioneSalaService.getPrenotazioneBySalaAfterDataDeleted(salaId, showAfter, deleted);
            model.addAttribute("prenotazioni", prenotazioni);
            return "prenotazioni";
        }
    }

    @PostMapping("/socio/prenotazioni/elimina")
    public String eliminaPrenotazione(@RequestParam(name = "prenotazione-id") Integer prenotazioneId,
                                      Model model,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        Optional<Socio> socioCookie = socioService.getSocioFromCookie(request, response);
        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }

        try {
            prenotazioneSalaService.deletePrenotazione(socioCookie.get().getId(), prenotazioneId);
            model.addAttribute("deleteSuccess", "true");
            return "redirect:/socio/prenotazioni?id=" + prenotazioneId;
        } catch (ValidationException e) {
            model.addAttribute("failed", "true");
            return "redirect:/socio/prenotazioni?id=" + prenotazioneId;
        } catch (EntityNotFoundException e) {
            return "redirect:/socio/prenotazioni";
        }
    }
}
