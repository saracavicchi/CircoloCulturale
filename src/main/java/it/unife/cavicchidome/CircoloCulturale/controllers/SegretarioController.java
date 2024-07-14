package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.exceptions.EntityAlreadyPresentException;
import it.unife.cavicchidome.CircoloCulturale.exceptions.ValidationException;
import it.unife.cavicchidome.CircoloCulturale.models.Saggio;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.services.BigliettoService;
import it.unife.cavicchidome.CircoloCulturale.services.CorsoService;
import it.unife.cavicchidome.CircoloCulturale.services.SaggioService;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/segretario")
public class SegretarioController {

    private final SocioService socioService;
    private final CorsoService corsoService;
    private final SaggioService saggioService;
    private final BigliettoService bigliettoService;

    public SegretarioController(SocioService socioService, CorsoService corsoService, SaggioService saggioService, BigliettoService bigliettoService) {
        this.socioService = socioService;
        this.corsoService = corsoService;
        this.saggioService = saggioService;
        this.bigliettoService = bigliettoService;
    }

    @GetMapping("/soci")
    public String viewSoci (@RequestParam(name = "initial") Optional<Character> initial,
                            @RequestParam(name = "deleted") Optional<Boolean> showDeleted,
                            Model model,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        Optional<Socio> segretario = socioService.setSocioFromCookie(request, response, model);
        if (segretario.isEmpty() || segretario.get().getSegretario() == null) {
            return "redirect:/";
        }

        List<Character> initials = socioService.getSociInitials();
        if (initial.isEmpty() || !initials.contains(initial.get())) {
            return "redirect:/segretario/soci?initial=" + initials.getFirst();
        }
        List<Socio> soci = socioService.findSocioCognomeInitial(initial.get(), showDeleted.orElse(false));
        model.addAttribute("soci", soci);
        model.addAttribute("initials", initials);
        return "soci";
    }

    @GetMapping("/nuovoSocio")
    public String nuovoSocio(Model model, HttpServletRequest request, HttpServletResponse response) {
        Optional<Socio> socio = socioService.setSocioFromCookie(request, response, model);
        if (socio.isEmpty() || socio.get().getSegretario() == null) {
            return "redirect:/";
        }
        return "nuovo-socio";
    }

    @PostMapping("/nuovoSocio")
    public String registerNewSocio(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String cf,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
            @RequestParam String birthplace,
            @RequestParam String country,
            @RequestParam String province,
            @RequestParam String city,
            @RequestParam String street,
            @RequestParam String houseNumber,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phoneNumber,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam BigDecimal price,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<Socio> segretario = socioService.getSocioFromCookie(request, response);
        if (segretario.isEmpty() || segretario.get().getSegretario() == null) {
            return "redirect:/";
        }

        try {
            Socio socio = socioService.newSocio(name, surname, cf, dob, birthplace, country, province, city, street, houseNumber, email, password, phoneNumber, Optional.of(price), photo);
            return "redirect:/socio/profile?socio-id=" + socio.getId();
        } catch (ValidationException validExc) {
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/segretario/nuovoSocio";
        } catch (EntityAlreadyPresentException entityExc) {
            redirectAttributes.addAttribute("alreadyPresent", "true");
            return "redirect:/segretario/nuovoSocio";
        }
    }

    @GetMapping("/corsi")
    public String segretarioViewCorsi(@RequestParam(name = "categoria") Optional<String> courseCategory,
                                      @RequestParam(name = "genere") Optional<String> courseGenre,
                                      @RequestParam(name = "livello") Optional<String> courseLevel,
                                      @RequestParam(name = "active") Optional<Boolean> active,
                                      Model model,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        Optional<Socio> segretario = socioService.setSocioFromCookie(request, response, model);
        if (segretario.isPresent() && segretario.get().getSegretario() == null) {
            return "redirect:/";
        }

        model.addAttribute("categorie", corsoService.getCategorie());
        model.addAttribute("generi", corsoService.getGeneri());
        model.addAttribute("livelli", corsoService.getLivelli());
        model.addAttribute("corsi", corsoService.filterCorsi(courseCategory, courseGenre, courseLevel, active));
        return "corsi-segretario";
    }

    @GetMapping("/saggi")
    public String viewSaggi(@RequestParam(name = "data") Optional<LocalDate> date,
                            @RequestParam(name = "deleted") Optional<Boolean> deleted,
                            Model model,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        Optional<Socio> segretario = socioService.setSocioFromCookie(request, response, model);
        if (segretario.isEmpty() || segretario.get().getSegretario() == null) {
            return "redirect:/";
        }

        model.addAttribute("saggi", saggioService.getSaggioAfterDateDeleted(date, deleted));
        return "saggi-segretario";
    }

    @GetMapping("/biglietti")
    public String viewBiglietti (@RequestParam(name = "saggioId") Optional<Integer> saggioId,
                                 @RequestParam(name = "nome") Optional<String> nome,
                                 @RequestParam(name = "cognome") Optional<String> cognome,
                                 @RequestParam(name = "deleted") Optional<Boolean> deleted,
                                 Model model,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        Optional<Socio> segretario = socioService.setSocioFromCookie(request, response, model);
        if (segretario.isEmpty() || segretario.get().getSegretario() == null) {
            return "redirect:/";
        }

        model.addAttribute("saggi", saggioService.findAllSaggi());
        model.addAttribute("biglietti", bigliettoService.findBigliettoNameSurnameSaggioDeleted(nome, cognome, saggioId, deleted));
        return "biglietti-segretario";
    }


}
