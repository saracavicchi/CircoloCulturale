package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Biglietto;
import it.unife.cavicchidome.CircoloCulturale.models.Tessera;
import it.unife.cavicchidome.CircoloCulturale.services.BigliettoService;
import it.unife.cavicchidome.CircoloCulturale.services.TesseraService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class PaymentController {

    private final BigliettoService bigliettoService;
    private final TesseraService tesseraService;

    public PaymentController(BigliettoService bigliettoService, TesseraService tesseraService) {
        this.bigliettoService = bigliettoService;
        this.tesseraService = tesseraService;
    }

    @GetMapping("/payment")
    public String viewPayment(@RequestParam(name = "biglietto-id") Optional<Integer> bigliettoId,
                              @RequestParam(name = "tessera-id") Optional<String> tesseraId,
                              @RequestParam(name = "redirect", defaultValue = "/") String redirect,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (bigliettoId.isPresent()) {
            Optional<Biglietto> biglietto = bigliettoService.findBigliettoById(bigliettoId.get());
            if (biglietto.isPresent()) {
                model.addAttribute("biglietto", biglietto.get());
                return "payment";
            }
        }

        if (tesseraId.isPresent()) {
            Optional<Tessera> tessera = tesseraService.findTesseraById(tesseraId.get());
            if (tessera.isPresent()) {
                model.addAttribute("tessera", tessera.get());
                return "payment";
            }
        }

        redirectAttributes.addAttribute("purchaseError", "true");
        return "redirect:" + redirect;
    }

    @PostMapping("/payment")
    public String purchaseTicket(@RequestParam(name = "biglietto-id") Optional<Integer> bigliettoId,
                                 @RequestParam(name = "tessera-id") Optional<String> tesseraId,
                                 @RequestParam(name = "pan") Optional<String> pan,
                                 @RequestParam(name = "expiry") Optional<LocalDate> expiry,
                                 @RequestParam(name = "ccv") Optional<String> ccv,
                                 @RequestParam(name = "redirect", defaultValue = "/") String redirect,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        if (bigliettoId.isPresent()) {
            try {
                bigliettoService.purchaseBiglietto(bigliettoId.get());
                redirectAttributes.addAttribute("success", "true");
                return "redirect:" + redirect;
            } catch (Exception e) {
                redirectAttributes.addAttribute("failed", "true");
                redirectAttributes.addAttribute("biglietto-id", "" + bigliettoId);
                redirectAttributes.addAttribute("redirect", redirect);
                return "redirect:/payment";
            }
        }

        if (tesseraId.isPresent()) {
            Optional<Tessera> tessera = tesseraService.findTesseraById(tesseraId.get());
            if (tessera.isPresent()) {
                if (tesseraService.purchaseTessera(tessera.get(), tesseraCvc.get(), request, response)) {
                    redirectAttributes.addAttribute("purchaseSuccess", "true");
                    return "redirect:" + redirect;
                }
            }
        }

        redirectAttributes.addAttribute("purchaseError", "true");
        return "redirect:" + redirect;
    }
}
