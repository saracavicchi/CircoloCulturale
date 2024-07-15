package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Biglietto;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.services.BigliettoService;
import it.unife.cavicchidome.CircoloCulturale.services.SaggioService;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/modifica")
    public String viewEditBiglietto(@RequestParam("bigliettoId") Integer bigliettoId,
                                    Model model,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        Optional<Socio> segretario = socioService.setSocioFromCookie(request, response, model);
        if (segretario.isEmpty() || segretario.get().getSegretario() == null) {
            return "redirect:/";
        }

        if (bigliettoId != null) {
            Optional<Biglietto> biglietto = bigliettoService.findBigliettoById(bigliettoId);
            if (biglietto.isPresent()) {
                model.addAttribute("biglietto", biglietto.get());
                return "biglietto-info-segretario";
            }
        }

        return "redirect:/segretario/biglietti";
    }

    @PostMapping("/modifica")
    public String editBiglietto(@RequestParam(name = "bigliettoId") Integer bigliettoId,
                                @RequestParam(name = "deleted") Boolean deleted,
                                @RequestParam(name = "pending") Character pending,
                                Model model,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                RedirectAttributes redirectAttributes) {
        Optional<Socio> segretario = socioService.getSocioFromCookie(request, response);
        if (segretario.isEmpty() || segretario.get().getSegretario() == null) {
            return "redirect:/";
        }

        try {
            bigliettoService.deleteBiglietto(bigliettoId, deleted);
            bigliettoService.confirmBiglietto(bigliettoId, (!pending.equals('c')));
            return "redirect:/biglietto/modifica?bigliettoId=" + bigliettoId;
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/biglietto/modifica?bigliettoId=" + bigliettoId;
        }
    }
}
