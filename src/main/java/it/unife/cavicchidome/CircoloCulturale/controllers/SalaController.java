package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.services.SalaService;
import it.unife.cavicchidome.CircoloCulturale.services.SedeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sala")
class SalaController {
    private final SalaService salaService;
    private final SedeService sedeService;

    public SalaController(SalaService salaService, SedeService sedeService) {
        this.salaService = salaService;
        this.sedeService = sedeService;
    }

    @GetMapping("/crea")
    public String creaSala(@RequestParam(name = "idSede") Integer idSede) {
        return "creazione-sala";
    }

    @PostMapping("/crea")
    public String newSala(@RequestParam(name = "numero") String numeroSala,
                          @RequestParam(name = "capienza") Integer capienza,
                          @RequestParam(name = "descrizione") String descrizione,
                          @RequestParam(name = "idSede") Integer idSede,
                          @RequestParam(name = "prenotabile", required = false, defaultValue = "false") Boolean prenotabile,
                          RedirectAttributes redirectAttributes
    ){
        try{
            if(salaService.newSala(numeroSala, capienza, descrizione, prenotabile, idSede))
                return "redirect:/sede/info?idSede=" + idSede;
            else{
                redirectAttributes.addAttribute("failed", "true");
                return "redirect:/sala/crea?idSede=" + idSede;
            }
        }
        catch (Exception e){
            if(e.getMessage().equals("Sala già presente"))
                redirectAttributes.addAttribute("alreadyPresent", "true");
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/sala/crea?idSede=" + idSede;
        }

    }
}
