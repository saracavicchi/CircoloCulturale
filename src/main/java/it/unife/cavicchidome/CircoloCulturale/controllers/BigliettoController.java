package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Biglietto;
import it.unife.cavicchidome.CircoloCulturale.services.BigliettoService;
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
@RequestMapping("/biglietto")
public class BigliettoController {

    private final BigliettoService bigliettoService;
    private final SaggioService saggioService;
    private final SocioService socioService;

    public BigliettoController(BigliettoService bigliettoService, SaggioService saggioService, SocioService socioService) {
        this.bigliettoService = bigliettoService;
        this.saggioService = saggioService;
        this.socioService = socioService;
    }

    @GetMapping("/info")
    public String viewInfo(@RequestParam(name = "id") Optional<Integer> bigliettoId,
                           Model model,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        socioService.setSocioFromCookie(request, response, model);

        if (bigliettoId.isPresent()) {
            Optional<Biglietto> biglietto = bigliettoService.findBigliettoById(bigliettoId.get());
            if (biglietto.isPresent()) {
                model.addAttribute("biglietto", biglietto.get());
                return "biglietto-info";
            }
        }
        model.addAttribute("biglietti", bigliettoService.findAllBiglietti());
        return "biglietti";
    }

}
