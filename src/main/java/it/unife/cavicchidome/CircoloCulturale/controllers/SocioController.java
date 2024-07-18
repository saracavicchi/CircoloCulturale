package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.Segretario;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.services.BigliettoService;
import it.unife.cavicchidome.CircoloCulturale.services.SaggioService;
import it.unife.cavicchidome.CircoloCulturale.services.SedeService;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/socio")
public class SocioController {

    private final SocioService socioService;
    private final SaggioService saggioService;
    private final BigliettoService bigliettoService;
    private final SedeService sedeService;

    @Value("${file.socio.upload-dir}")
    String uploadDir;

    SocioController(SocioService socioService, SaggioService saggioService, BigliettoService bigliettoService, SedeService sedeService) {
        this.saggioService = saggioService;
        this.bigliettoService = bigliettoService;
        this.socioService = socioService;
        this.sedeService = sedeService;
    }


    @GetMapping("/profile")
    public String showSocioProfile(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("socio-id") Optional<Integer> socioId
    ) {
        // TODO: far vedere se si tratta di socio cancellato
        Optional<Socio> socioCookie = socioService.setSocioFromCookie(request, response, model);

        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }

        Socio socio;
        if (socioId.isPresent() && socioService.findSocioByIdAll(socioId.get()).isPresent()) { //TODO: ATTENZIONE Cosi restituisce anche soci cancellati
            if (socioCookie.get().getSegretario() != null) {
                socio = socioService.findSocioByIdAll(socioId.get()).get();
            } else {
                return "redirect:/";
            }
        } else {
            socio = socioCookie.get();
        }

        model.addAttribute("uploadDir", uploadDir);
        model.addAttribute("placeholderImage", "profilo.jpg");
        model.addAttribute("socio", socio);

        // Restituisce la vista socio-profile.jsp con i dati
        return "socio-profile";
    }

    // TODO: rimpiazzare saggi con biglietti
    /*@GetMapping("/saggi")
    public String getSocioSaggi(Model model,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        Optional<Socio> socioCookie = socioService.setSocioFromCookie(request, response, model);
        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }
        //TODO: Eliminare riferimenti tra saggi e soci
        //model.addAttribute("saggi", socioCookie.get().getSaggi());
        return "socio-saggi";
    }*/

    @GetMapping("/biglietti")
    public String viewBiglietti (@RequestParam(name = "saggioId") Optional<Integer> saggioId,
                                 @RequestParam(name = "deleted") Optional<Boolean> deleted,
                                 Model model,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        Optional<Socio> socio = socioService.setSocioFromCookie(request, response, model);
        if (!socio.isPresent()) {
            return "redirect:/";
        }

        model.addAttribute("biglietti", bigliettoService.findBigliettiSocio(socio.get().getId()));
        model.addAttribute("saggi", saggioService.findSaggiSocio(socio.get().getId()));
        return "socio-biglietti";
    }


    @GetMapping("/corsi")
    public String getSocioCorsi(Model model,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        Optional<Socio> socioCookie = socioService.setSocioFromCookie(request, response, model);
        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("corsi", socioCookie.get().getCorsi());
        return "socio-corsi";
    }

    @PostMapping("/profile") //TODO: RENDERE TRANSAZIONALE
    public String changeSocioData(
            @RequestParam("socio-id") Optional<Integer> socioId,
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String cf,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
            @RequestParam String birthplace,
            @RequestParam String state,
            @RequestParam String province,
            @RequestParam String city,
            @RequestParam String street,
            @RequestParam String houseNumber,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam("photo") MultipartFile photo,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<Socio> socioCookie = socioService.getSocioFromCookie(request, response);

        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }

        Socio socio;
        String redirectTo;
        if (socioId.isPresent()) {
            if (socioId.get().equals(socioCookie.get().getId())) {
                socio = socioCookie.get();
                redirectTo = "";
            } else if (socioService.findSocioById(socioId.get()).isPresent()) {
                if (socioCookie.get().getSegretario() != null) {
                    socio = socioService.findSocioById(socioId.get()).get();
                    redirectTo = "?socio-id=" + socioId.get();
                } else {
                    return "redirect:/";
                }
            } else {
                redirectAttributes.addAttribute("failed", "true");
                return "redirect:/socio/profile?socio-id=" + socioId.get();
            }
        } else {
            socio = socioCookie.get();
            redirectTo = "";
        }

        try {
            socioService.editSocioAndUtente(socio.getId(),
                    socio.getUtente().getId(),
                    Optional.of(name),
                    Optional.of(surname),
                    Optional.of(cf),
                    Optional.of(dob),
                    Optional.of(birthplace),
                    Optional.of(state),
                    Optional.of(province),
                    Optional.of(city),
                    Optional.of(street),
                    Optional.of(houseNumber),
                    Optional.of(email),
                    Optional.of(phoneNumber),
                    Optional.of(photo));
            redirectAttributes.addAttribute("success", "true");
            return "redirect:/socio/profile" + redirectTo;
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/socio/profile" + redirectTo;
        }
    }

    @PostMapping("/modificaPassword")
    public String modificaPassword(
            @RequestParam("socio-id") Optional<Integer> socioId,
            @RequestParam("old-password") Optional<String> passwordAttuale,
            @RequestParam("new-password") String passwordNuova,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<Socio> socioCookie = socioService.getSocioFromCookie(request, response);

        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }

        Socio socio;
        String redirectTo;

        if (socioId.isPresent()) {
            if (socioId.get().equals(socioCookie.get().getId())) {
                socio = socioCookie.get();
                redirectTo = "";
            } else if (socioService.findSocioById(socioId.get()).isPresent()) {
                if (socioCookie.get().getSegretario() != null) {
                    socio = socioService.findSocioById(socioId.get()).get();
                    redirectTo = "?socio-id=" + socioId.get();
                } else {
                    return "redirect:/";
                }
            } else {
                redirectAttributes.addAttribute("passwordFailed", "true");
                return "redirect:/socio/profile?socio-id=" + socioId.get();
            }
        } else {
            socio = socioCookie.get();
            redirectTo = "";
        }

        try {
            socioService.updateSocioPassword(socio.getId(), passwordAttuale, passwordNuova);
            redirectAttributes.addAttribute("passwordSuccess", "true");
            return "redirect:/socio/profile" + redirectTo;
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("passwordFailed", "true");
            return "redirect:/socio/profile" + redirectTo;
        }
    }

    @PostMapping("/elimina") //TODO: GESTIRE TRANSAZIONALITà
    public String eliminaSocio(
            @RequestParam("socio-id") Optional<Integer> socioId,
            @RequestParam("delete") Optional<Boolean> delete,
            HttpServletResponse response,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Socio> socioCookie = socioService.getSocioFromCookie(request, response);

        if (socioCookie.isEmpty()) {
            return "redirect:/";
        }

        Socio socio;
        String redirectTo;

        if (socioId.isPresent()) {
            if (socioId.get().equals(socioCookie.get().getId())) {
                socio = socioCookie.get();
                redirectTo = "";
                Segretario segretario = socioCookie.get().getSegretario();
                if( segretario != null){
                   if(sedeService.findSedeByIdSegretario(segretario.getId()).isPresent()){
                          redirectAttributes.addAttribute("deleteFailed", "true");
                          System.out.println("Non si può disiscrivere un segretario associato ad una sede");
                          return "redirect:/socio/profile?socio-id=" + socioId.get(); //NON si può disiscrivere un segretario associato ad una sede
                   }
                }
            } else if (socioService.findSocioByIdAll(socioId.get()).isPresent()) {
                if (socioCookie.get().getSegretario() != null) {
                    socio = socioService.findSocioByIdAll(socioId.get()).get();
                    redirectTo = "?socio-id=" + socioId.get();
                } else {
                    return "redirect:/";
                }
            } else {
                redirectAttributes.addAttribute("deleteFailed", "true");
                return "redirect:/socio/profile?socio-id=" + socioId.get();
            }
        } else {
            socio = socioCookie.get();
            redirectTo = "";
        }

        socioService.deleteSocioAndUser(socio.getId(), delete);

        redirectAttributes.addAttribute("deleteSuccess", "true");
        return "redirect:/socio/profile" + redirectTo;
    }
}



