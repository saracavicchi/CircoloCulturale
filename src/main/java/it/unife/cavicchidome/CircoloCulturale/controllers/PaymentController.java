package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Biglietto;
import it.unife.cavicchidome.CircoloCulturale.models.Tessera;
import it.unife.cavicchidome.CircoloCulturale.services.BigliettoService;
import it.unife.cavicchidome.CircoloCulturale.services.TesseraService;
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
import java.util.Optional;

@Controller
public class PaymentController {

    private final BigliettoService bigliettoService;
    private final TesseraService tesseraService;

    @Autowired
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
            Optional<Biglietto> biglietto = bigliettoService.findBigliettoById(bigliettoId.get()); //TODO: vengono mostrati i biglietti cancellati
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
                                 @RequestParam Optional<Boolean> cancelled,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        if (cancelled.isPresent()) {
            redirectAttributes.addAttribute("cancelled", "true");
            return "redirect:" + redirect;
        }

        // Check if the pan is 16 digits long, the expiry date is in the future and the ccv is 3 digits long
        if (pan.isPresent() && expiry.isPresent() && ccv.isPresent()) {
            if (pan.get().length() != 16 || expiry.get().isBefore(LocalDate.now()) || ccv.get().length() != 3) {
                redirectAttributes.addAttribute("failed", "true");
                if (bigliettoId.isPresent())
                    redirectAttributes.addAttribute("biglietto-id", "" + bigliettoId);
                else if (tesseraId.isPresent())
                    redirectAttributes.addAttribute("tessera-id", tesseraId.get());
                redirectAttributes.addAttribute("redirect", redirect);
                return "redirect:/payment";
            }
        }

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
            try {
                tesseraService.purchaseTessera(tesseraId.get());
                redirectAttributes.addAttribute("success", "true");
                return "redirect:" + redirect;
            } catch (Exception e) {
                redirectAttributes.addAttribute("failed", "true");
                redirectAttributes.addAttribute("tessera-id", tesseraId.get());
                redirectAttributes.addAttribute("redirect", redirect);
                return "redirect:/payment";
            }
        }

        redirectAttributes.addAttribute("error", "true");
        return "redirect:" + redirect;
    }
}
