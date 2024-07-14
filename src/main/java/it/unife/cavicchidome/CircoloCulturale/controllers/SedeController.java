package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Sede;
import it.unife.cavicchidome.CircoloCulturale.services.SedeService;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
    public String creaSede(Model model) {
        model.addAttribute("sociInfo", socioService.findSociPossibiliSegretari());
        return "creazione-sede";
    }

    @PostMapping("/crea")
    public String newSede(@RequestParam(name = "nome") String nome,
                          @RequestParam(name= "stato") String stato,
                          @RequestParam(name = "citta") String citta,
                          @RequestParam(name = "provincia") String provincia,
                          @RequestParam(name = "via") String via,
                          @RequestParam(name = "numeroCivico") String numeroCivico,
                          @RequestParam(name = "segretari") Integer idSegretario,
                          @RequestParam(name = "adminSegretario", required = false, defaultValue = "false") boolean admin,
                          @RequestParam(name = "areaRistoro", required = false, defaultValue = "false") boolean areaRistoro,
                          @RequestParam(name = "orarioApertura") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<LocalTime> orariApertura,
                          @RequestParam(name = "orarioChiusura") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<LocalTime> orariChiusura,
                          @RequestParam(name = "chiusura", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<LocalDate> chiusura,
                          RedirectAttributes redirectAttributes
                          ) {
        try{
            if(!sedeService.newSede(nome, stato, citta, provincia, via, numeroCivico, areaRistoro, chiusura, orariApertura, orariChiusura, idSegretario, admin)){
                redirectAttributes.addAttribute("fail", "true");
                return "redirect:/sede/crea";
            }
            return "redirect:/sede/info";
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

    @GetMapping("/modifica")
    public String modificaSede(@RequestParam(name = "idSede") Integer idSede, Model model) {
        Optional<Sede> sedeOpt = sedeService.findSedeById(idSede);
        if(sedeOpt.isEmpty() || !sedeOpt.isPresent()){
            return "redirect:/sede/info";
        }
        model.addAttribute("sociInfo", socioService.findSociPossibiliSegretari());
        model.addAttribute("sede", sedeOpt.get());
        return "modifica-sede";
    }

    @PostMapping("/modifica")
    public String modificaSede(@RequestParam(name = "idSede") Integer idSede,
                               @RequestParam(name = "nome") String nome,
                               @RequestParam(name = "areaRistoro", required = false, defaultValue = "false") boolean areaRistoro,
                               @RequestParam(name = "chiusura", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<LocalDate> chiusura,
                               @RequestParam(name = "deletedChiusura", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<LocalDate> deletedChiusura,
                               @RequestParam(name = "isAdmin", required = false, defaultValue = "false") boolean isAdmin,
                               @RequestParam(name = "segretari") Integer idSegretario,
                               @RequestParam(name = "adminSegretario", required = false, defaultValue = "false") boolean adminNuovo,
                               RedirectAttributes redirectAttributes
                              ) {
        try{
            if(!sedeService.updateSede(idSede, nome, areaRistoro, chiusura, deletedChiusura, isAdmin, idSegretario, adminNuovo)){
                redirectAttributes.addAttribute("fail", "true");
                return "redirect:/sede/modifica?idSede=" + idSede;
            }
            return "redirect:/sede/info?id=" + idSede;
        } catch (Exception e) {
            String message = e.getMessage();
            System.out.println(message);
            redirectAttributes.addAttribute("fail", "true");
            if(message != null && message.equals("Nome già presente"))
                redirectAttributes.addAttribute("nameAlreadyPresent", "true");
            return "redirect:/sede/modifica?idSede=" + idSede;
        }
    }

    @PostMapping("/elimina")
    public String eliminaSede(@RequestParam(name = "idSede") Integer idSede,
                              RedirectAttributes redirectAttributes
    ){
        try{
            if(sedeService.deleteSede(idSede))
                return "redirect:/sede/info";
            else{
                redirectAttributes.addAttribute("failed", "true");
                return "redirect:/sede/modifica?idSede=" + idSede;
            }
        }
        catch (Exception e){
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/sede/modifica?idSede=" + idSede;
        }
    }


}