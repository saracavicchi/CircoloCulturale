package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Sede;
import it.unife.cavicchidome.CircoloCulturale.services.SedeService;
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
@RequestMapping("/sede")
public class SedeController {

    private final SedeService sedeService;
    private final SocioService socioService;

    public SedeController(SedeService sedeService,
                          SocioService socioService) {
        this.sedeService = sedeService;
        this.socioService = socioService;
    }

    @GetMapping("/info")
    public String sedi(@RequestParam(name = "id") Optional<Integer> sedeId,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       Model model) {

        socioService.setSocioFromCookie(request, response, model);

        // TODO: fix JSP return
        if (sedeId.isPresent()) {
            Optional<Sede> sede = sedeService.findSedeById(sedeId.get());
            if (sede.isPresent()) {
                model.addAttribute("sede", sede.get());
            }
            return "sede-info";
        } else {
            model.addAttribute("sedi", sedeService.getAllSedi());
            return "sedi";
        }
    }

    @GetMapping("/crea")
    public String creaSede() {
        return "creazione-sede";
    }

    @PostMapping("/crea")
    public String newSede(@RequestParam(name = "nome") String nome,
                          @RequestParam(name= "stato") String stato,
                          @RequestParam(name = "citta") String citta,
                          @RequestParam(name = "provincia") String provincia,
                          @RequestParam(name = "via") String via,
                          @RequestParam(name = "numeroCivico") String numeroCivico,
                          @RequestParam(name = "areaRistoro", required = false, defaultValue = "false") boolean areaRistoro,
                          RedirectAttributes redirectAttributes
                          ) {
        try{
            if(!sedeService.newSede(nome, stato, citta, provincia, via, numeroCivico, areaRistoro)){
                redirectAttributes.addAttribute("fail", "true");
                return "redirect:/sede/crea";
            }
            return "redirect:/sede/sedi";
        } catch (Exception e) {
            String message = e.getMessage();
            System.out.println(message);
            redirectAttributes.addAttribute("fail", "true");
            if(message != null && message.equals("Indirizzo già presente"))
                redirectAttributes.addAttribute("addressAlreadyPresent", "true");
            else if(message != null && message.equals("Nome già presente"))
                redirectAttributes.addAttribute("nameAlreadyPresent", "true");
            return "redirect:/sede/crea";
        }
    }


}