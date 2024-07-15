package it.unife.cavicchidome.CircoloCulturale.controllers;

import it.unife.cavicchidome.CircoloCulturale.models.*;
import it.unife.cavicchidome.CircoloCulturale.repositories.CorsoRepository;
import it.unife.cavicchidome.CircoloCulturale.services.BigliettoService;
import it.unife.cavicchidome.CircoloCulturale.services.SaggioService;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import it.unife.cavicchidome.CircoloCulturale.services.CorsoService;
import it.unife.cavicchidome.CircoloCulturale.services.UtenteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/saggio")
public class SaggioController {

    private final SocioService socioService;
    private final SaggioService saggioService;
    private final UtenteService utenteService;
    private final BigliettoService bigliettoService;
    private final CorsoService corsoService;
    private final CorsoRepository corsoRepository;

    @Value("${file.saggio.upload-dir}")
    String uploadDir;

    SaggioController(SaggioService saggioService, SocioService socioService, UtenteService utenteService, BigliettoService bigliettoService, CorsoService corsoService, CorsoRepository corsoRepository) {
        this.saggioService = saggioService;
        this.socioService = socioService;
        this.utenteService = utenteService;
        this.bigliettoService = bigliettoService;
        this.corsoService = corsoService;
        this.corsoRepository = corsoRepository;
    }

    @GetMapping("/info")
    public String viewSaggi(@RequestParam(name = "id") Optional<Integer> saggioId,
                            @RequestParam(name = "data") Optional<LocalDate> date,
                            @RequestParam(name = "deleted") Optional<Boolean> deleted,
                            Model model,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        socioService.setSocioFromCookie(request, response, model);

        if (saggioId.isPresent()) {
            Optional<Saggio> saggio = saggioService.findSaggioById(saggioId.get());
            if (saggio.isPresent()) {
                model.addAttribute("saggio", saggio.get());
                model.addAttribute("availableTickets", saggioService.getAvailableTickets(saggio.get()));
                return "saggio-info";
            }
        }
        model.addAttribute("saggi", saggioService.getSaggioAfterDateDeleted(date, deleted));
        return "saggi";
    }

    @GetMapping("/iscrizione")
    public String viewTicketSaggio(@RequestParam(name = "id") Optional<Integer> saggioId,
                                   Model model,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        socioService.setSocioFromCookie(request, response, model);
        if (saggioId.isPresent()) {
            Optional<Saggio> saggio = saggioService.findSaggioById(saggioId.get());
            if (saggio.isPresent()) {
                model.addAttribute("saggio", saggio.get());
                model.addAttribute("availableTickets", saggioService.getAvailableTickets(saggio.get()));
                return "saggio-iscrizione";
            }
        }
        return "redirect:/saggio/info";
    }

    @PostMapping("/iscrizione")
    public String ticketSaggio(@RequestParam(name = "socio-id") Optional<Integer> socioId,
                               @RequestParam Optional<String> name,
                               @RequestParam Optional<String> surname,
                               @RequestParam Optional<String> cf,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dob,
                               @RequestParam Optional<String> birthplace,
                               @RequestParam Optional<String> country,
                               @RequestParam Optional<String> province,
                               @RequestParam Optional<String> city,
                               @RequestParam Optional<String> street,
                               @RequestParam Optional<String> houseNumber,
                               @RequestParam Integer quantity,
                               @RequestParam(name = "saggio-id") Integer saggioId,
                               RedirectAttributes redirectAttributes) {

        try {
            Biglietto biglietto = bigliettoService.newBiglietto(socioId, name, surname, cf, dob, birthplace, country, province, city, street, houseNumber, quantity, saggioId);
            redirectAttributes.addAttribute("biglietto-id", biglietto.getId());
            redirectAttributes.addAttribute("redirect", "/biglietto/info?id=" + biglietto.getId());
            return "redirect:/payment";
        } catch (Exception e) {
            redirectAttributes.addAttribute("failed", "true");
            return "redirect:/saggio/iscrizione?id=" + saggioId;
        }

    }

    @GetMapping("/crea")
    public String viewCreateSaggio(Model model, HttpServletRequest request, HttpServletResponse response) {
        socioService.setSocioFromCookie(request, response, model);
        if(corsoService.aggiungiCorsiBaseRuolo(request, response, model)){
            return "creazione-saggio";
        }
        return "redirect:/saggio/info"; //TODO: aggiungere messaggio di errore
    }

    @PostMapping("/crea")
    public String creaSaggio(@RequestParam("nome") String nome,
                             @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                             @RequestParam("numeroPartecipanti") int numeroPartecipanti,
                             @RequestParam(value = "descrizione", required = false) String descrizione,
                             @RequestParam(value = "orarioInizio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) Optional<LocalTime> orarioInizio,
                             @RequestParam(value = "orarioFine", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) Optional<LocalTime> orarioFine,
                             @RequestParam("stato") String stato,
                             @RequestParam("provincia") String provincia,
                             @RequestParam("citta") String citta,
                             @RequestParam("via") String via,
                             @RequestParam("numeroCivico") String numeroCivico,
                             @RequestParam("corsi") List<Integer> corsiIds,
                             @RequestParam(value = "photo", required = false) MultipartFile photo,
                             RedirectAttributes redirectAttributes
    ) {
       try{
            if(saggioService.newSaggio(nome, data, numeroPartecipanti,descrizione, orarioInizio, orarioFine, stato, provincia, citta, via, numeroCivico, corsiIds, photo)){
                return "redirect:/saggio/info";
            }
            redirectAttributes.addAttribute("fail", "true");
            return "redirect:/saggio/crea";
        } catch (Exception e) {
            String message = e.getMessage();
            System.out.println(message);
            redirectAttributes.addAttribute("fail", "true");
            if(message != null && message.equals("Data già presente"))
                redirectAttributes.addAttribute("dateAlreadyPresent", "true");
            else if(message != null && message.equals("Nome già presente"))
                redirectAttributes.addAttribute("nameAlreadyPresent", "true");
            return "redirect:/saggio/crea";
        }

    }

    @GetMapping("/modifica")
    public String viewModificaSaggio(@RequestParam("saggioId") Integer saggioId, Model model, HttpServletRequest request, HttpServletResponse response) {
        Optional<Socio> segretario = socioService.setSocioFromCookie(request, response, model);
        if (segretario.isEmpty() || segretario.get().getSegretario() == null) {
            return "redirect:/";
        }
        Optional<Saggio> saggio = saggioService.findSaggioById(saggioId);
        if (saggio.isPresent() && corsoService.aggiungiCorsiBaseRuolo(request, response, model)) {
            model.addAttribute("saggio", saggio.get());
            model.addAttribute("uploadDir", uploadDir);
            model.addAttribute("placeholderImage", "profilo.jpg");
            return "modifica-saggio";
        }

        return "redirect:/saggio/info";
    }

    @PostMapping("/modifica")
    public String modificaSaggio(@RequestParam("saggioId") Integer saggioId,
                                 @RequestParam("nome") String nome,
                                 @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                 @RequestParam("numeroPartecipanti") int numeroPartecipanti,
                                 @RequestParam(value = "descrizione", required = false) String descrizione,
                                 @RequestParam(value = "orarioInizio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) Optional<LocalTime> orarioInizio,
                                 @RequestParam(value = "orarioFine", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) Optional<LocalTime> orarioFine,
                                 @RequestParam("stato") String stato,
                                 @RequestParam("provincia") String provincia,
                                 @RequestParam("citta") String citta,
                                 @RequestParam("via") String via,
                                 @RequestParam("numeroCivico") String numeroCivico,
                                 @RequestParam("corsi") List<Integer> corsiIds,
                                 @RequestParam(value = "photo", required = false) MultipartFile photo,
                                 RedirectAttributes redirectAttributes
    ) {
        try {
            if(saggioService.updateSaggio(saggioId, nome, data, numeroPartecipanti, descrizione, orarioInizio, orarioFine, stato, provincia, citta, via, numeroCivico, corsiIds, photo)){
                return "redirect:/saggio/info";
            }
            redirectAttributes.addAttribute("fail", "true");
            return "redirect:/saggio/modifica?saggioId=" + saggioId;
        } catch (Exception e) {
            String message = e.getMessage();
            System.out.println(message);
            redirectAttributes.addAttribute("fail", "true");
            if(message != null && message.equals("Data già presente"))
                redirectAttributes.addAttribute("dateAlreadyPresent", "true");
            else if(message != null && message.equals("Nome già presente"))
                redirectAttributes.addAttribute("nameAlreadyPresent", "true");
            return "redirect:/saggio/modifica?saggioId=" + saggioId;
        }
    }

    @PostMapping("/elimina")
    public String eliminaSaggio(@RequestParam("saggioId") Integer saggioId, RedirectAttributes redirectAttributes) {
        try{
            saggioService.deleteSaggio(saggioId);
        } catch (Exception e) {
            redirectAttributes.addAttribute("fail", "true"); //TODO: aggiungere messaggio di errore
            return "redirect:/saggio/info";
        }

        return "redirect:/saggio/info";
    }



}
