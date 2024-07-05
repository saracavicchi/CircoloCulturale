package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Saggio;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.models.Biglietto;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.services.BigliettoService;
import it.unife.cavicchidome.CircoloCulturale.services.SaggioService;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import it.unife.cavicchidome.CircoloCulturale.services.UtenteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

/*
@Controller
@RequestMapping("/saggio")
public class SaggioController {

    private final SocioService socioService;
    private final SaggioService saggioService;
    private final UtenteService utenteService;
    private final BigliettoService bigliettoService;

    SaggioController(SaggioService saggioService, SocioService socioService, UtenteService utenteService, BigliettoService bigliettoService) {
        this.saggioService = saggioService;
        this.socioService = socioService;
        this.utenteService = utenteService;
        this.bigliettoService = bigliettoService;
    }

    @GetMapping("/info")
    public String viewSaggi(@RequestParam(name = "id") Optional<Integer> saggioId,
                            Model model,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        socioService.getSocioFromCookie(request, response, model);

        if (saggioId.isPresent()) {
            Optional<Saggio> saggio = saggioService.findSaggioById(saggioId.get());
            if (saggio.isPresent()) {
                model.addAttribute("saggio", saggio.get());
                model.addAttribute("availableTickets", saggioService.getAvailableTickets(saggio.get()));
                return "saggio-info";
            }
        }
        model.addAttribute("saggi", saggioService.findAllSaggi());
        return "saggi";
    }

    @GetMapping("/iscrizione")
    public String viewEnrollSaggio(@RequestParam(name = "id") Optional<Integer> saggioId,
                               Model model,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        socioService.getSocioFromCookie(request, response, model);
        if (saggioId.isPresent()) {
            Optional<Saggio> saggio = saggioService.findSaggioById(saggioId.get());
            if (saggio.isPresent()) {
                model.addAttribute("saggio", saggio.get());
                model.addAttribute("availableTickets", saggioService.getAvailableTickets(saggio.get()));
                return "saggio-iscrizione";
            }
        }
        return "redirect:/saggio/info";
    }

    @PostMapping("/iscrizione")
    public String enrollSaggio(@RequestParam(name = "socio-id") Optional<Integer> socioId,
                               @RequestParam Optional<String> ticketUser,
                               @RequestParam String name,
                               @RequestParam String surname,
                               @RequestParam String cf,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
                               @RequestParam String birthplace,
                               @RequestParam String state,
                               @RequestParam String province,
                               @RequestParam String city,
                               @RequestParam String street,
                               @RequestParam String houseNumber,
                               @RequestParam Integer quantity,
                               @RequestParam(name = "saggio-id") Integer saggioId,
                               RedirectAttributes redirectAttributes) {
        if (socioId.isPresent()) {
            Optional<Socio> socio = socioService.findSocioById(socioId.get());
            if (socio.isPresent()) {
                if (ticketUser.isPresent()) {
                    if (ticketUser.get().equals("self")) {
                        Optional<Saggio> saggio;
                        if ((saggio = saggioService.findSaggioById(saggioId)).isPresent()) {
                            Biglietto biglietto = bigliettoService.createBigliettoAndSave(socio.get().getUtente(), saggio.get(), quantity, Instant.now(), 'p', false, false);
                        } else {
                            redirectAttributes.addAttribute("failed1", "true");
                            return "redirect:/saggio/iscrizione?id=" + saggioId;
                        }
                    }
                }
            } else {
                redirectAttributes.addAttribute("failed2", "true");
                return "redirect:/saggio/iscrizione?id=" + saggioId;
            }
        }

        Optional<Utente> utente;
        if ((utente = utenteService.getUtenteByCf(cf)).isPresent()) {
            Optional<Saggio> saggio = saggioService.findSaggioById(saggioId);
            if (!saggio.isPresent()) {
                redirectAttributes.addAttribute("failed4", "true");
                return "redirect:/saggio/iscrizione?id=" + saggioId;
            }
            Biglietto biglietto = bigliettoService.createBigliettoAndSave(utente.get(), saggio.get(), quantity, Instant.now(), 'p', false, false);
            redirectAttributes.addAttribute("success", "true");
            return "redirect:/saggio/iscrizione?id=" + saggioId;
        }
        else {
            if (!utenteService.validateUserInfo(name, surname, cf, dob, birthplace, state, province, city, street, houseNumber)) {
                redirectAttributes.addAttribute("failed3", "true");
                return "redirect:/saggio/iscrizione?id=" + saggioId;
            } else {
                Utente newUtente = utenteService.createUtenteAndSave(name, surname, cf, dob, birthplace, state, province, city, street, houseNumber);
                Optional<Saggio> saggio = saggioService.findSaggioById(saggioId);
                if (!saggio.isPresent()) {
                    redirectAttributes.addAttribute("failed4", "true");
                    return "redirect:/saggio/iscrizione?id=" + saggioId;
                }
                Biglietto biglietto = bigliettoService.createBigliettoAndSave(newUtente, saggio.get(), quantity, Instant.now(), 'p', false, false);

                redirectAttributes.addAttribute("success", "true");
                return "redirect:/saggio/iscrizione?id=" + saggioId;
            }


        }
    }
}
*/