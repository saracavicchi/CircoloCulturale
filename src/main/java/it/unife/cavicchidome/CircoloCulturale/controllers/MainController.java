package it.unife.cavicchidome.CircoloCulturale.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/") // http://localhost:8080/
    public String index() {
        return "index";
    }
}
