package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Saggio;
import it.unife.cavicchidome.CircoloCulturale.services.SaggioService;
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
@RequestMapping("/saggio")
public class SaggioController {

    private final SocioService socioService;
    private final SaggioService saggioService;

    SaggioController(SaggioService saggioService, SocioService socioService) {
        this.saggioService = saggioService;
        this.socioService = socioService;
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
    public String enrollSaggio(@RequestParam(name = "id") Optional<Integer> saggioId,
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
}
