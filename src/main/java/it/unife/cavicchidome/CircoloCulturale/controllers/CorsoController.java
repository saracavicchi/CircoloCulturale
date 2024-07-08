package it.unife.cavicchidome.CircoloCulturale.controllers;
import it.unife.cavicchidome.CircoloCulturale.models.CalendarioCorso;
import it.unife.cavicchidome.CircoloCulturale.models.Corso;
import it.unife.cavicchidome.CircoloCulturale.models.Docente;
import it.unife.cavicchidome.CircoloCulturale.repositories.CorsoRepository;
import it.unife.cavicchidome.CircoloCulturale.services.SocioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import it.unife.cavicchidome.CircoloCulturale.services.SalaService;
import it.unife.cavicchidome.CircoloCulturale.services.DocenteService;
import it.unife.cavicchidome.CircoloCulturale.services.CorsoService;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;

@Controller
public class CorsoController {
    private final CorsoRepository corsoRepository;
    private SalaService salaService;
    private DocenteService docenteService;
    private SocioService socioService;
    private CorsoService corsoService;
    @Value("${file.corso.upload-dir}")
    private String uploadDir;

    public CorsoController(
            CorsoService corsoService,
            SalaService salaService,
            DocenteService docenteService,
            SocioService socioService,
            CorsoRepository corsoRepository) {
        this.salaService = salaService;
        this.docenteService = docenteService;
        this.socioService = socioService;
        this.corsoService = corsoService;
        this.corsoRepository = corsoRepository;
    }

    @GetMapping("/creazioneCorso")
    public String creaCorso(Model model) {

        //TODO: Aggiungi controllo per verificare che si tratta di ADMIN

        // Ottenere le sale dal servizio e aggiungerle al model
        model.addAttribute("sale", salaService.findAll());

        // Ottenere i soci dal servizio e aggiungerli al model
        List<Object[]> sociInfo = socioService.findSociNotSegretari();
        model.addAttribute("sociInfo", sociInfo);

        // Aggiungere i giorni della settimana al model come interi
        List<Integer> giorniSettimana = Arrays.asList(1, 2, 3, 4, 5); // Assuming 1 = Monday, 2 = Tuesday, etc.
        model.addAttribute("giorniSettimana", giorniSettimana);

        return "creazioneCorso";
    }

    @PostMapping("/creazioneCorso")
    public String creaCorso(@RequestParam("descrizione") String descrizione,
                            @RequestParam("genere") String genere,
                            @RequestParam("livello") String livello,
                            @RequestParam("categoria") String categoria,
                            @RequestParam("idSala") Integer idSala,
                            @RequestParam("docenti") List<String> docenti,
                            @RequestParam("giorni") List<Integer> giorni,
                            @RequestParam("orariInizio") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) List<LocalTime> orarioInizio,
                            @RequestParam("orariFine") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) List<LocalTime> orarioFine,
                            @RequestParam ("stipendi")List<Integer> stipendi,
                            @RequestParam("photo") MultipartFile foto,
                            Model model,
                            RedirectAttributes redirectAttributes
    ) {

        // Validate course data first
        boolean isValid = corsoService.validateCourseData(descrizione, genere, livello, categoria, idSala, docenti, stipendi, giorni, orarioInizio, orarioFine);
        if (!isValid) {
            // Handle validation failure (e.g., log the error, return "errorView", throw an exception)
            redirectAttributes.addAttribute("fail", "true");
            return "redirect:/creazioneCorso"; // Adjust "errorView" to your actual error view name
        }

        // If validation passes, proceed to save course information
        boolean saveSuccess = corsoService.saveCourseInformation(descrizione, genere, livello, categoria, idSala, docenti,stipendi, giorni, orarioInizio, orarioFine, foto);
        if (!saveSuccess) {
            redirectAttributes.addAttribute("fail", "true");
            return "redirect:/creazioneCorso";
        }

        return "redirect:/"; //TODO: Adjust "successView" to your actual success view name
    }

    @GetMapping("/editCorso")
    public String viewEdit(
            @RequestParam("idCorso") Integer idCorso,
            Model model
    ) {
        // Recupera le informazioni del corso tramite il suo ID
        Optional<Corso> corso = corsoService.findById(idCorso);
        if (!corso.isPresent()) {
            // TODO:Gestisci il caso in cui il corso non viene trovato (es. reindirizzamento a una pagina di errore)
            return "redirect:/paginaErrore";
        }
         // Aggiungi il corso al modello per poterlo visualizzare nella pagina JSP
        model.addAttribute("corso", corso.get());

        model.addAttribute("uploadDir", uploadDir);
        model.addAttribute("placeholderImage", "profilo.jpg");



        return "editCorso"; // Nome della JSP da visualizzare
    }

    @PostMapping("/editCorso")
    public String updateCorso(
            @RequestParam("idCorso") Integer idCorso,
            @RequestParam("descrizione") String descrizione,
            @RequestParam("genere") String genere,
            @RequestParam("livello") String livello,
            @RequestParam("categoria") String categoria,
            @RequestParam("photo") Optional<MultipartFile> photo,
            RedirectAttributes redirectAttributes
    ) {
        // Validate course data first
        boolean isValid = corsoService.validateBasicInfo(descrizione, genere, livello, categoria);
        if (!isValid) {
            // Handle validation failure (e.g., log the error, return "errorView", throw an exception)
            redirectAttributes.addAttribute("fail", "true");
            return "redirect:/"; //TODO: vedere come gestire meglio
        }

        Optional<Corso> corsoOpt = corsoService.findById(idCorso);
        if (!corsoOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Corso non trovato.");
            return "redirect:/paginaErrore";
        }

        //TODO: Rendere transazionale
        //TODO: verificare unique
        Corso corso = corsoOpt.get();
        corso.setDescrizione(descrizione);
        corso.setGenere(genere);
        corso.setLivello(livello);
        corso.setCategoria(categoria);


        corsoRepository.save(corso); // Assuming you have a save method in your service

        return "redirect:/"; //TODO: pagina visualizzazione corsi
    }

    @GetMapping("/modificaDocenti")
    public String modificaDocenti(
            @RequestParam("idCorso") Integer idCorso,
            Model model
    ) {
        Optional<Corso> corsoOpt = corsoService.findById(idCorso);
        if (!corsoOpt.isPresent()) {
            // Gestisci il caso in cui il corso non viene trovato
            return "redirect:/paginaErrore";
        }
        Corso corso = corsoOpt.get();

        // Aggiungi il corso al modello
        model.addAttribute("corso", corso);

        // Aggiungi l'elenco dei docenti correnti al modello
        List<Docente> docentiCorso = new ArrayList<>(corso.getDocenti());
        Collections.sort(docentiCorso, Comparator.comparingInt(Docente::getId));
        model.addAttribute("docentiCorso", docentiCorso);

        // Ottieni e aggiungi l'elenco di tutti i docenti disponibili al modello
        List<Docente> tuttiIDocenti = docenteService.findAll();
        model.addAttribute("tuttiIDocenti", tuttiIDocenti);

        List<Object[]> sociInfo = socioService.findSociNotDocentiAndNotSegretariByIdCorso(idCorso);
        model.addAttribute("sociInfo", sociInfo);

        return "modificaDocenti"; // Nome della JSP da visualizzare
    }

    @PostMapping("/modificaDocenti")
    public String updateDocenti(
            @RequestParam("idCorso") Integer idCorso,
            @RequestParam("docentiDaEliminare") Optional<List<Integer>> deletedDocentiId,
            @RequestParam("stipendiAttuali") List<Integer> stipendiAttuali,
            @RequestParam("nuoviDocenti") Optional<List<String>> docentiCf,
            @RequestParam("stipendi") Optional<List<Integer>> stipendi,
            RedirectAttributes redirectAttributes
    ) {

        boolean updateSuccess = corsoService.updateDocenti(idCorso, deletedDocentiId, docentiCf, stipendiAttuali, stipendi);

        if (!updateSuccess) {
            redirectAttributes.addAttribute("fail", "true");
            return "redirect:/modificaDocenti?idCorso=" + idCorso ; //TODO: vedere come gestire meglio
        }




        return "redirect:/"; //TODO: Adjust the redirect as necessary
    }



    @GetMapping("/modificaCalendario")
    public String viewModificaCalendario(
            @RequestParam("idCorso") Integer idCorso,
            Model model
    ) {
        Optional<Corso> corsoOpt = corsoService.findById(idCorso);
        if (!corsoOpt.isPresent()) {
            // Gestisci il caso in cui il corso non viene trovato
            return "redirect:/paginaErrore";
        }
        Corso corso = corsoOpt.get();

        // Aggiungi il corso al modello
        model.addAttribute("corso", corso);
        Set<CalendarioCorso> calendarioCorso = corso.getCalendarioCorso();
        Set<CalendarioCorso> calendariAttivi = calendarioCorso.stream()
                .filter(CalendarioCorso::getActive)
                .collect(Collectors.toSet());
        model.addAttribute("calendarioCorso", calendariAttivi);

        // Aggiungi al modello le informazioni aggiuntive necessarie per la pagina di modifica, come le sale disponibili
        model.addAttribute("sale", salaService.findAll());

        // Restituisci il nome della JSP da visualizzare
        return "modificaCalendario"; // Nome della JSP da visualizzare
    }

    @PostMapping("/modificaCalendario")
    public String updateCalendario(
            @RequestParam("idCorso") Integer idCorso,
            @RequestParam("giorni") List<Integer> giorni,
            @RequestParam("orariInizio") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) List<LocalTime> orariInizio,
            @RequestParam("orariFine") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) List<LocalTime> orariFine,
            @RequestParam("idSala") Integer idSala,
            RedirectAttributes redirectAttributes
    ) {
        boolean updateSuccess = corsoService.updateCourseSchedule(idCorso, giorni, orariInizio, orariFine, idSala);

        if (!updateSuccess) {
            redirectAttributes.addAttribute("fail", "true");
            return "redirect:/modificaCalendario?idCorso=" + idCorso ; //TODO: vedere come gestire meglio
        }



        redirectAttributes.addAttribute("successMessage", "Calendario aggiornato con successo.");
        return "redirect:/"; //TODO: pagina visualizzazione corsi
    }
}


