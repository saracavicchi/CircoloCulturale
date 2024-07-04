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

        socioService.setSocioFromCookie(request, response, model);

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
    public String viewTicketSaggio(@RequestParam(name = "id") Optional<Integer> saggioId,
                                   Model model,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        socioService.setSocioFromCookie(request, response, model);
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
    public String ticketSaggio(@RequestParam(name = "socio-id") Optional<Integer> socioId,
                               @RequestParam Optional<String> name,
                               @RequestParam Optional<String> surname,
                               @RequestParam Optional<String> cf,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dob,
                               @RequestParam Optional<String> birthplace,
                               @RequestParam Optional<String> country,
                               @RequestParam Optional<String> province,
                               @RequestParam Optional<String> city,
                               @RequestParam Optional<String> street,
                               @RequestParam Optional<String> houseNumber,
                               @RequestParam Integer quantity,
                               @RequestParam(name = "saggio-id") Integer saggioId,
                               RedirectAttributes redirectAttributes) {

        try {
            Biglietto biglietto = bigliettoService.newBiglietto(socioId, name, surname, cf, dob, birthplace, country, province, city, street, houseNumber, quantity, saggioId);
            redirectAttributes.addAttribute("biglietto-id", biglietto.getId());
            redirectAttributes.addAttribute("redirect", "/biglietto/info?id=" + biglietto.getId());
            return "redirect:/payment";
        } catch (Exception e) {
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/saggio/iscrizione?id=" + saggioId;
        }

    }
}
