package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Sede;
import it.unife.cavicchidome.CircoloCulturale.services.SedeService;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
/*
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

        socioService.getSocioFromCookie(request, response, model);

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
}
*/