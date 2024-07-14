package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.services.SalaService;
import it.unife.cavicchidome.CircoloCulturale.services.SedeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sede/sala")
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
                return "redirect:/sede/sala/crea?idSede=" + idSede;
            }
        }
        catch (Exception e){
            if(e.getMessage().equals("Sala già presente"))
                redirectAttributes.addAttribute("alreadyPresent", "true");
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/sede/sala/crea?idSede=" + idSede;
        }

    }

    @GetMapping("/modifica")
    public String modificaSala(@RequestParam(name = "idSede") Integer idSede, Model model) {

        model.addAttribute("sale", salaService.findAllBySedeId(idSede));

        return "modifica-sale";
    }

    @PostMapping("/modifica")
    public String modificaSala(@RequestParam(name = "idSala") Integer idSala,
                              @RequestParam(name = "idSede") Integer idSede,
                              @RequestParam(name = "numero") String numeroSala,
                              @RequestParam(name = "descrizione") String descrizione,
                              @RequestParam(name = "prenotabile", required = false, defaultValue = "false") Boolean prenotabile,
                              RedirectAttributes redirectAttributes
    ){
        try{
            if(salaService.updateSala(idSala, numeroSala, descrizione, prenotabile))
                return "redirect:/sede/info?idSede=" + idSede;
            else{
                redirectAttributes.addAttribute("failed", "true");
                return "redirect:/sede/sala/modifica?idSede=" + idSede;
            }
        }
        catch (Exception e){
            if(e.getMessage().equals("Sala già presente"))
                redirectAttributes.addAttribute("alreadyPresent", "true");
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/sede/sala/modifica?idSede=" + idSede;
        }
    }

    @PostMapping("/elimina")
    public String eliminaSala(@RequestParam(name = "idSala") Integer idSala,
                              @RequestParam(name = "idSede") Integer idSede,
                              RedirectAttributes redirectAttributes
    ){
        try{
            if(salaService.deleteSala(idSala))
                return "redirect:/sede/info?idSede=" + idSede;
            else{
                redirectAttributes.addAttribute("failed", "true");
                return "redirect:/sede/sala/modifica?idSede=" + idSede;
            }
        }
        catch (Exception e){
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/sede/sala/modifica?idSede=" + idSede;
        }
    }
}
