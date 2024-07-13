package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.*;
import it.unife.cavicchidome.CircoloCulturale.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class CorsoService {

    private final UtenteRepository utenteRepository;
    private final OrarioSedeService orarioSedeService;
    private final SalaService salaService;
    private CorsoRepository corsoRepository;
    private SalaRepository salaRepository;
    private SocioRepository socioRepository;
    private DocenteRepository docenteRepository;
    private CalendarioCorsoRepository calendarioCorsoRepository;
    private SedeService sedeService;
    private final SocioService socioService;

    @Value("${file.corso.upload-dir}")
    String uploadCorsoDir;


    public CorsoService(
            CorsoRepository corsoRepository,
            SalaRepository salaRepository,
            SocioRepository socioRepository,
            UtenteRepository utenteRepository,
            DocenteRepository docenteRepository,
            CalendarioCorsoRepository calendarioCorsoRepository,
            OrarioSedeService orarioSedeService,
            SalaService salaService,
            SedeService sedeService,
            SocioService socioService
    ) {
        this.corsoRepository = corsoRepository;
        this.salaRepository = salaRepository;
        this.socioRepository = socioRepository;
        this.utenteRepository = utenteRepository;
        this.docenteRepository = docenteRepository;
        this.calendarioCorsoRepository = calendarioCorsoRepository;
        this.orarioSedeService = orarioSedeService;
        this.salaService = salaService;
        this.sedeService = sedeService;
        this.socioService = socioService;
    }

    public boolean validateBasicInfo(String descrizione, String genere, String livello, String categoria) {
        String regex = "^[a-zA-Z- ]+$";
        return descrizione != null && descrizione.matches(regex) &&
                genere != null && genere.matches(regex) && genere.length() <= 20 &&
                livello != null && livello.matches(regex) && livello.length() <= 20 &&
                categoria != null && !categoria.isEmpty();
    }

    public boolean validateDocentiAndStipendi(List<String> docentiCf, List<Integer> stipendi) {
        if (docentiCf == null || docentiCf.isEmpty() || !validateStipendi(stipendi)) return false;
        return true;
    }

    private boolean validateStipendi(List<Integer> stipendi){
        if (stipendi == null || stipendi.isEmpty()) return false;
        for (Integer stipendio : stipendi) {
            if (stipendio == null || stipendio < 10000 || stipendio > 100000) return false;
        }
        return true;
    }

    public boolean validateCalendarioAndSala(List<Integer> giorni, List<LocalTime> orarioInizio, List<LocalTime> orarioFine, Integer idSala) {
        if (idSala == null || giorni == null || giorni.isEmpty() || orarioInizio == null || orarioInizio.isEmpty() || orarioFine == null || orarioFine.isEmpty()) return false;
        if (orarioInizio.size() != orarioFine.size()) return false;
        Optional<Sala> salaOpt = salaRepository.findById(idSala);
        if (!salaOpt.isPresent()) {
            return false; // Sala not found
        }
        for (int i = 0; i < giorni.size(); i++) {
            Integer giorno = giorni.get(i);
            List<LocalTime> orariAperturaChiusura = orarioSedeService.findOrarioAperturaChiusuraByIdSedeAndGiornoSettimana(sedeService.findSedeByIdSala(idSala).get().getId(), Weekday.values()[giorno]);
            LocalTime orarioApertura = orariAperturaChiusura.get(0);
            LocalTime orarioChiusura = orariAperturaChiusura.get(1);
            LocalTime inizio = orarioInizio.get(giorno-1);
            LocalTime fine = orarioFine.get(giorno-1);
            if (inizio == null || fine == null || !inizio.isBefore(fine) || inizio.isBefore(orarioApertura) || fine.isAfter(orarioChiusura)) {
                return false;
            }
        }
        return true;
    }

    public boolean validateCourseData(
            String descrizione,
            String genere,
            String livello,
            String categoria,
            Integer idSala,
            List<String> docentiCf,
            List<Integer> stipendi,
            List<Integer> giorni,
            List<LocalTime> orarioInizio,
            List<LocalTime> orarioFine) {

        return validateBasicInfo(descrizione, genere, livello, categoria) &&
                validateDocentiAndStipendi(docentiCf, stipendi) &&
                validateCalendarioAndSala(giorni, orarioInizio, orarioFine, idSala);
    }


    @Transactional(readOnly = true)
    public Corso findCorsoByCategoriaGenereLivello(String categoria, String genere, String livello) {
        return corsoRepository.findByCategoriaAndGenereAndLivello(categoria, genere, livello).orElse(null);
    }

    @Transactional
    public boolean saveCourseInformation(
            String descrizione,
            String genere,
            String livello,
            String categoria,
            Integer idSala,
            List<String> docentiCf,
            List<Integer> stipendi,
            List<Integer> giorni,
            List<LocalTime> orarioInizio,
            List<LocalTime> orarioFine,
            MultipartFile photo) {

        boolean reactivated = false;

        Optional<Corso> corsoIdentical = corsoRepository.findByCategoriaAndGenereAndLivello(categoria, genere, livello);
        if(corsoIdentical.isPresent() ) {
            if(corsoIdentical.get().getActive() == true)
                return false; //Corso Attivo già esistente
            else {
                corsoIdentical.get().setActive(true);
                reactivated = true;
            }
        }
        System.out.println("reactivated: " + reactivated);

        // Step 2: Fetch Sala
        Sala sala = salaRepository.findById(idSala).orElse(null);
        if (sala == null) {
            return false; // Sala does not exist
        }
        Set<Docente> docenti = new HashSet<>();
        for (int i = 0; i < docentiCf.size(); i++) {
            String cf = docentiCf.get(i);
            // Step 3: Find User by CF
            Optional<Utente> utenteOpt = utenteRepository.findByCf(cf);
            if (!utenteOpt.isPresent()) {
                return false; // Utente does not exist
            }
            Utente utente = utenteOpt.get();

            // Step 4: Check if Utente is a Socio
            Optional<Socio> socioOpt = socioRepository.findById(utente.getId());
            if (!socioOpt.isPresent()) {
                return false; // Socio does not exist
            }
            Socio socio = socioOpt.get();

            // Step 5: Check if Socio is not a Docente
            if (socio.getDocente() == null) {
                // Socio is not a Docente, proceed to save Docente information
                Docente docente = new Docente();
                docente.setSocio(socio);
                docente.setStipendio(BigDecimal.valueOf(stipendi.get(i)));
                docenteRepository.save(docente);
                docenti.add(docente);
            }
            else{
                if (BigDecimal.valueOf(stipendi.get(i)).compareTo(socio.getDocente().getStipendio()) > 0){
                    socio.getDocente().setStipendio(BigDecimal.valueOf(stipendi.get(i)));
                    docenteRepository.save(socio.getDocente());
                }
                docenti.add(socio.getDocente());
            }

        }

        if(checkScheduleOverlap(null, giorni, orarioInizio, orarioFine, idSala) == false){
            return false;
        }


        Corso corso;
        // Step 6: Save Course Information
        if(!reactivated){
            corso = new Corso();
            corso.setGenere(genere);
            corso.setLivello(livello);
            corso.setCategoria(categoria);
        }
        else{
            corso=corsoIdentical.get();
        }

        corso.setDescrizione(descrizione);
        corso.setIdSala(sala);
        corso.setDocenti(docenti);
        corso.setActive(true);
        corsoRepository.save(corso);

        if(photo != null){
            String filename = saveCorsoPicture(photo, categoria, corso.getId()); //TODO: controllare che funzioni alla creazione di un corso
            corso.setUrlFoto(filename);
        }
        //corsoRepository.save(corso);

        /*for (Integer giorno: giorni) {
            CalendarioCorso calendarioCorso = new CalendarioCorso();
            CalendarioCorsoId calendarioCorsoId = new CalendarioCorsoId();
            calendarioCorsoId.setIdCorso(corso.getId()); // Assuming getId() method exists in Corso
            try{
                Weekday weekday = Weekday.fromDayNumber(giorno);
                calendarioCorsoId.setGiornoSettimana(weekday);
                System.out.println(weekday);
            }catch (IllegalArgumentException e){
                return false;
            }

            calendarioCorso.setId(calendarioCorsoId);
            calendarioCorso.setIdCorso(corso); // Set the Corso object
            calendarioCorso.setOrarioInizio(orarioInizio.get(giorno-1));
            calendarioCorso.setOrarioFine(orarioFine.get(giorno-1));
            calendarioCorso.setActive(true);
            calendarioCorsoRepository.save(calendarioCorso);
        }*/

        Set<CalendarioCorso> nuovoCalendarioCorso = new HashSet<>();
        for (Integer giorno: giorni) {
            Weekday weekday;
            try{
                weekday = Weekday.fromDayNumber(giorno);
                System.out.println(weekday);
            }catch (IllegalArgumentException e){
                return false;
            }

            if(reactivated) {
                Optional<CalendarioCorso> existingEntryOpt = calendarioCorsoRepository.findByCorsoAndGiornoSettimanaId(corso.getId(), weekday);

                if (existingEntryOpt.isPresent()) {
                    System.out.println(existingEntryOpt.get().getGiornoSettimana());
                    CalendarioCorso existingEntry = existingEntryOpt.get();
                    System.out.println(orarioInizio.get(giorno - 1));
                    existingEntry.setOrarioInizio(orarioInizio.get(giorno - 1));
                    existingEntry.setOrarioFine(orarioFine.get(giorno - 1));
                    existingEntry.setActive(true);
                    calendarioCorsoRepository.save(existingEntry);
                    nuovoCalendarioCorso.add(existingEntry);
                } else {
                    // Create new entry
                    CalendarioCorso newCalendario = new CalendarioCorso();
                    CalendarioCorsoId calendarioCorsoId = new CalendarioCorsoId();
                    ;
                    calendarioCorsoId.setGiornoSettimana(weekday);
                    calendarioCorsoId.setIdCorso(corso.getId());
                    newCalendario.setId(calendarioCorsoId);
                    newCalendario.setIdCorso(corso);
                    newCalendario.setActive(true);
                    newCalendario.setOrarioInizio(orarioInizio.get(giorno - 1));
                    newCalendario.setOrarioFine(orarioFine.get(giorno - 1));
                    calendarioCorsoRepository.save(newCalendario);
                    nuovoCalendarioCorso.add(newCalendario);
                }
            }else{
                CalendarioCorso newCalendario = new CalendarioCorso();
                CalendarioCorsoId calendarioCorsoId = new CalendarioCorsoId();
                ;
                calendarioCorsoId.setGiornoSettimana(weekday);
                calendarioCorsoId.setIdCorso(corso.getId());
                newCalendario.setId(calendarioCorsoId);
                newCalendario.setIdCorso(corso);
                newCalendario.setActive(true);
                newCalendario.setOrarioInizio(orarioInizio.get(giorno - 1));
                newCalendario.setOrarioFine(orarioFine.get(giorno - 1));
                calendarioCorsoRepository.save(newCalendario);
                nuovoCalendarioCorso.add(newCalendario);
            }

        }


        if (!corso.getIdSala().equals(idSala)) {
            Sala newSala = salaRepository.findById(idSala).orElseThrow(() -> new IllegalStateException("Sala not found"));
            corso.setIdSala(newSala);

        }

        corso.setCalendarioCorso(nuovoCalendarioCorso);
        corsoRepository.save(corso);

        return true;
    }

    public boolean checkScheduleOverlap(Optional<Corso> corso, List<Integer> giorni, List<LocalTime> orarioInizio, List<LocalTime> orarioFine, Integer idSala) {
        //Controllo sovrapposizione oraria di corsi che si tengono nella stessa sala
        boolean sovrapposizione = false;
        for (int i = 0; i < giorni.size(); i++) {
            Integer giorno = giorni.get(i);
            LocalTime inizio = orarioInizio.get(giorno-1);
            LocalTime fine = orarioFine.get(giorno-1);

            // Trova corsi nel CalendarioCorso che si sovrappongono per orario
            List<CalendarioCorso> corsiSovrapposti = calendarioCorsoRepository.findCorsiSovrapposti(Weekday.fromDayNumber(giorno) , inizio, fine, idSala );
            for (CalendarioCorso calendarioCorso : corsiSovrapposti) {
                System.out.println(calendarioCorso.getIdCorso().getDescrizione());
                Corso corsoSovrapposto = calendarioCorso.getIdCorso();
                if (calendarioCorso.getIdCorso().getActive()==true && corsoSovrapposto.getActive()==true) {
                    if(corso.isPresent() && corso.get().getId().equals(corsoSovrapposto.getId())){
                        continue; //si esclude corso stesso dal controllo di sovrapposizione
                    }
                    sovrapposizione = true;
                    break;
                }
            }
            if (sovrapposizione) break;
        }

        if (sovrapposizione) {
            // Gestisci la sovrapposizione (es. restituendo false o lanciando un'eccezione)
            return false;
        }
        else return true;
    }

    public boolean checkScheduleOverlap(Optional<Corso> corso, List<CalendarioCorso> calendarioCorso, Integer idSala) {
        //Controllo sovrapposizione oraria di corsi che si tengono nella stessa sala
        boolean sovrapposizione = false;
        for (int i = 0; i < calendarioCorso.size(); i++) {
            Integer giorno = calendarioCorso.get(i).getId().getGiornoSettimana().getDayNumber();
            LocalTime inizio = calendarioCorso.get(i).getOrarioInizio();
            LocalTime fine = calendarioCorso.get(i).getOrarioFine();

            // Trova corsi nel CalendarioCorso che si sovrappongono per orario
            List<CalendarioCorso> corsiSovrapposti = calendarioCorsoRepository.findCorsiSovrapposti(Weekday.fromDayNumber(giorno) , inizio, fine, idSala );
            for (CalendarioCorso calendario : corsiSovrapposti) {
                System.out.println(calendario.getIdCorso().getDescrizione());
                Corso corsoSovrapposto = calendario.getIdCorso();
                if (calendario.getIdCorso().getActive()==true && corsoSovrapposto.getActive()==true) {
                    if(corso.isPresent() && corso.get().getId().equals(corsoSovrapposto.getId())){
                        continue; //si esclude corso stesso dal controllo di sovrapposizione
                    }
                    sovrapposizione = true;
                    break;
                }
            }
            if (sovrapposizione) break;
        }

        if (sovrapposizione) {
            // Gestisci la sovrapposizione (es. restituendo false o lanciando un'eccezione)
            return false;
        }
        else return true;
    }

    @Transactional
    public boolean checkDocentiScheduleOverlap(List<String> docentiCf){
        for(String cf: docentiCf){
            Optional<Docente> docente = docenteRepository.findByCf(cf);
            if(!docente.isPresent()) {
                return false; //TODO: gestire caso in cui docente non esiste: eccezione
            }
            Integer idDocente = docente.get().getId();
            List<Corso> corsiInsegnati = corsoRepository.findCorsiByDocenteId(idDocente);
            if(!corsiInsegnati.isEmpty()){
                for(Corso corso: corsiInsegnati){
                    if(corso.getActive()){
                        Set<CalendarioCorso> calendarioCorsoSet = corso.getCalendarioCorso();
                        List<CalendarioCorso> calendarioCorso = new ArrayList<>(calendarioCorsoSet);
                        if(!checkScheduleOverlap(Optional.of(corso), calendarioCorso, corso.getIdSala().getId())){
                            return false;
                        }

                    }
                }
            }

        }

        return true;
    }

    String saveCorsoPicture(MultipartFile picture, String categoria, Integer idCorso) {
        if (picture == null || picture.isEmpty()) {
            return null;
        }

        String originalFilename = picture.getOriginalFilename();
        if (originalFilename == null) {
            return null;
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = categoria + idCorso + extension;
        //String filename = filenameConSpazi.replace(" ", "");

        try {
            // Percorso relativo alla directory resources/static del progetto
            String relativePath = "static/images/corsoPhotos";
            // Costruisce il percorso completo utilizzando il percorso del progetto
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "src/main/resources", relativePath);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path picturePath = uploadPath.resolve(filename);
            System.out.println("Tentativo di salvataggio in: " + picturePath.toAbsolutePath());

            picture.transferTo(picturePath);

            if (Files.exists(picturePath)) {
                System.out.println("File salvato correttamente in" + picturePath.toAbsolutePath());
            } else {
                System.out.println("Il file non è stato salvato.");
                return null;
            }

            // Restituisce il percorso relativo per l'accesso via URL
            //return Paths.get(relativePath, filename).toString().replace("\\", "/");
            return filename;
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
            exc.printStackTrace();
            return null;
        }
    }


    @Transactional
    public List<Corso> filterCorsi(Optional<String> category,
                                   Optional<String> genre,
                                   Optional<String> level,
                                   Optional<Integer> socioId) {
        List<Corso> corsi = corsoRepository.findAll();

        if (category.isPresent() && !category.get().isEmpty()) {
            List<Corso> categoryFilteredCorsi = new ArrayList<>();
            for (Corso c : corsi) {
                if (c.getCategoria().equals(category.get())) {
                    categoryFilteredCorsi.add(c);
                }
            }
            corsi = categoryFilteredCorsi;
        }

        if (genre.isPresent() && !genre.get().isEmpty()) {
            List<Corso> genreFilteredCorsi = new ArrayList<>();
            for (Corso c : corsi) {
                if (c.getGenere().equals(genre.get())) {
                    genreFilteredCorsi.add(c);
                }
            }
            corsi = genreFilteredCorsi;
        }

        if (level.isPresent() && !level.get().isEmpty()) {
            List<Corso> levelFilteredCorsi = new ArrayList<>();
            for (Corso c : corsi) {
                if (c.getLivello().equals(level.get())) {
                    levelFilteredCorsi.add(c);
                }
            }
            corsi = levelFilteredCorsi;
        }

        if (socioId.isPresent()) {
            List<Corso> docenteFilteredCorsi = new ArrayList<>();
            for (Corso c : corsi) {
                for (Docente d : c.getDocenti()) {
                    if (d.getSocio().getId().equals(socioId.get())) {
                        docenteFilteredCorsi.add(c);
                        break;
                    }
                }
            }

            List<Corso> segretarioFilteredCorsi = new ArrayList<>();
            for (Corso c : docenteFilteredCorsi) {
                if (c.getIdSala().getIdSede().getSegretario().getId().equals(socioId.get())) {
                    segretarioFilteredCorsi.add(c);
                }
            }
            corsi = segretarioFilteredCorsi;
        }

        return corsi;
    }

    @Transactional
    public Boolean isEnrolled(Corso corso, Socio socio) {
        return corso.getSoci().contains(socio);
    }

    @Transactional
    public Boolean isAvailable(Corso corso) {
        return (corso.getIdSala().getCapienza() > corso.getSoci().size());
    }


    @Transactional
    public List<String> getCategorie() {
        return corsoRepository.findDistinctCategoria();
    }

    @Transactional
    public List<String> getGeneri() {
        return corsoRepository.findDistinctGenere();
    }

    @Transactional
    public List<String> getLivelli() {
        return corsoRepository.findDistinctLivello();
    }

    @Transactional
    public Optional<Corso> findCorsoById(Integer idCorso) {
        return corsoRepository.findByIdActive(idCorso);
    }

    @Transactional
    public void enroll(Integer socioId, Integer corsoId) throws EntityNotFoundException {
        Corso corso = corsoRepository.getReferenceById(corsoId);
        if (corso == null) {
            throw new EntityNotFoundException("Corso not found");
        }
        Socio socio = socioRepository.getReferenceById(socioId);
        if (socio == null) {
            throw new EntityNotFoundException("Socio not found");
        }
        corso.getSoci().add(socio);
        corsoRepository.save(corso);
    }

    @Transactional
    public boolean updateBasicCourseInfo(Integer idCorso, String descrizione, String genere, String livello, String categoria) {
        // Validate course data first
        boolean isValid = validateBasicInfo(descrizione, genere, livello, categoria);
        if (!isValid) {
            return false;
        }

        Optional<Corso> corsoOpt = corsoRepository.findByIdActive(idCorso);
        Optional<Corso> corsoIdenticalOpt = corsoRepository.findByCategoriaAndGenereAndLivello(descrizione, genere, livello);

        if (!corsoOpt.isPresent() || corsoIdenticalOpt.isPresent()) { //Se id non esiste o corso con stesse caratteristiche esiste già
            return false;
        }

        // Update course data
        Corso corso = corsoOpt.get();
        corso.setDescrizione(descrizione);
        corso.setGenere(genere);
        corso.setLivello(livello);
        corso.setCategoria(categoria);

        corsoRepository.save(corso);
        return true;
    }

    @Transactional
    public boolean updateCourseSchedule(Integer idCorso, List<Integer> giorni, List<LocalTime> orarioInizio, List<LocalTime> orarioFine, Integer idSala) {
        Optional<Corso> corsoOpt = corsoRepository.findByIdActive(idCorso);
        if (!corsoOpt.isPresent()) {
            return false; // Course not found
        }
        Corso corso = corsoOpt.get();


        if(checkScheduleOverlap(corsoOpt, giorni, orarioInizio, orarioFine, idSala) == false){
            return false;
        }

        // Delete existing CalendarioCorso entries for this course
        List<CalendarioCorso> existingEntries = calendarioCorsoRepository.findByCorsoId(idCorso);
        existingEntries.forEach(calendarioCorso -> calendarioCorso.setActive(false));

        // Update existing entries or create new ones and collect IDs of days to keep

        Set<CalendarioCorso> nuovoCalendarioCorso = new HashSet<>();
        for (Integer giorno: giorni) {
            Weekday weekday;
            try{
                weekday = Weekday.fromDayNumber(giorno);
                System.out.println(weekday);
            }catch (IllegalArgumentException e){
                return false;
            }

            Optional<CalendarioCorso> existingEntryOpt = calendarioCorsoRepository.findByCorsoAndGiornoSettimanaId(idCorso, weekday);



            if (existingEntryOpt.isPresent()) {
                System.out.println(existingEntryOpt.get().getGiornoSettimana());
                CalendarioCorso existingEntry = existingEntryOpt.get();
                System.out.println(orarioInizio.get(giorno-1));
                existingEntry.setOrarioInizio(orarioInizio.get(giorno-1));
                existingEntry.setOrarioFine(orarioFine.get(giorno-1));
                existingEntry.setActive(true);
                calendarioCorsoRepository.save(existingEntry);
                nuovoCalendarioCorso.add(existingEntry);
            } else {
                // Create new entry
                CalendarioCorso newCalendario = new CalendarioCorso();
                CalendarioCorsoId calendarioCorsoId = new CalendarioCorsoId();;
                calendarioCorsoId.setGiornoSettimana(weekday);
                calendarioCorsoId.setIdCorso(idCorso);
                newCalendario.setId(calendarioCorsoId);
                newCalendario.setIdCorso(corso);
                newCalendario.setActive(true);
                newCalendario.setOrarioInizio(orarioInizio.get(giorno-1));
                newCalendario.setOrarioFine(orarioFine.get(giorno-1));
                calendarioCorsoRepository.save(newCalendario);
                nuovoCalendarioCorso.add(newCalendario);

            }
        }


        if (!corso.getIdSala().equals(idSala)) {
            Sala newSala = salaRepository.findById(idSala).orElseThrow(() -> new IllegalStateException("Sala not found"));
            corso.setIdSala(newSala);

        }

        corso.setCalendarioCorso(nuovoCalendarioCorso);
        corsoRepository.save(corso);


        return true; // Successfully updated the course schedule/
    }




    @Transactional
    public Optional<Corso> findById(Integer idCorso) {
        return corsoRepository.findByIdActive(idCorso);
    }

    @Transactional
    public List<Object[]> findDocentiByCorsoId(Integer corsoId) {
        return docenteRepository.findDocentiByCorsoId(corsoId);
    }

    @Transactional(readOnly = true) //da eliminare
    public List<CalendarioCorso> findCalendarioByCorsoId(Integer corsoId) {
        return calendarioCorsoRepository.findByCorsoId(corsoId);
    }

    @Transactional
    public List<Corso> findAll() {
        return corsoRepository.findAll();
    }

    @Transactional
    public boolean updateDocenti(
            Integer idCorso,
            Optional<List<Integer>> deletedDocentiId,
            Optional<List<String>> docentiCf,
            List<Integer> stipendiAttuali,
            Optional<List<Integer>> stipendi
    ) {
        Optional<Corso> corsoOpt = corsoRepository.findByIdActive(idCorso);
        if (!corsoOpt.isPresent()) {
            return false; // Corso non trovato
        }
        Corso corso = corsoOpt.get();

        Set<Docente> docenti = new HashSet<>();

        if (stipendiAttuali.size() != corso.getDocenti().size() || !validateStipendi(stipendiAttuali)) {
            return false;
        }
        Collections.sort(stipendiAttuali);
        List<Docente> docentiList = new ArrayList<>(corso.getDocenti());
        Collections.sort(docentiList, Comparator.comparingInt(Docente::getId)); //ordino docenti in base a id crescente
        for (int i = 0; i < docentiList.size(); i++) {
            Integer stipendio = stipendiAttuali.get(i);
            Docente docente = docentiList.get(i);
            boolean eliminato = false;
            if(deletedDocentiId.isPresent()){
                for(Integer id : deletedDocentiId.get()){
                    if(docente.getId() == id){
                        eliminato=true;
                        break;
                    }
                }
            }
            if (!eliminato) {
                if (BigDecimal.valueOf(stipendio).compareTo(docente.getStipendio()) > 0){
                    docente.setStipendio(BigDecimal.valueOf(stipendio));
                    docenteRepository.save(docente);
                }
                docenti.add(docente);
            }

        }

        if (docentiCf.isPresent() && stipendi.isPresent()) {
            if(!validateDocentiAndStipendi(docentiCf.get(), stipendi.get())){
                return false;
            }
            List<String> cfList = docentiCf.get();
            List<Integer> stipendiList = stipendi.get();

            for (int i = 0; i < cfList.size(); i++) {
                String cf = cfList.get(i);
                // Step 3: Find User by CF
                Optional<Utente> utenteOpt = utenteRepository.findByCf(cf);
                if (!utenteOpt.isPresent()) {
                    return false; // Utente does not exist
                }
                Utente utente = utenteOpt.get();

                // Step 4: Check if Utente is a Socio
                Optional<Socio> socioOpt = socioRepository.findById(utente.getId());
                if (!socioOpt.isPresent()) {
                    return false; // Socio does not exist
                }
                Socio socio = socioOpt.get();

                // Step 5: Check if Socio is not a Docente
                if (socio.getDocente() == null) {
                    // Socio is not a Docente, proceed to save Docente information
                    Docente docente = new Docente();
                    docente.setSocio(socio);
                    docente.setStipendio(BigDecimal.valueOf(stipendiList.get(i)));
                    docenteRepository.save(docente);
                    docenti.add(docente);
                }
                else{
                    if (BigDecimal.valueOf(stipendiList.get(i)).compareTo(socio.getDocente().getStipendio()) > 0){
                        Docente docente = socio.getDocente();
                        docente.setStipendio(BigDecimal.valueOf(stipendiList.get(i)));
                        docenteRepository.save(docente);
                    }

                    docenti.add(socio.getDocente());
                }
            }

        }
        // Salvataggio delle modifiche al corso
        corsoRepository.save(corso);
        corso.setDocenti(docenti);
        if(deletedDocentiId.isPresent()){
            for(Integer docenteId : deletedDocentiId.get()){
                List<Corso> corsiInsegnati = corsoRepository.findCorsiByDocenteId(corso.getId());
                if(!corsiInsegnati.isEmpty()) {
                    Docente docente = docenteRepository.findById(docenteId).orElseThrow(() -> new IllegalStateException("Docente not found"));
                    docente.setActive(false);
                    docenteRepository.save(docente);
                }
            }
        }

        return true; // Operazione completata con successo
    }

    public boolean corsoOverlap(Integer salaId, LocalDate date, LocalTime start, LocalTime end) {
        return corsoRepository.findOverlapCorso(salaId, Weekday.fromDayNumber(date.getDayOfWeek().getValue()), start, end).isPresent();
    }

    @Transactional
    public boolean deleteCourse(Integer idCorso) {
        Optional<Corso> corsoOpt = corsoRepository.findByIdActive(idCorso);
        if (!corsoOpt.isPresent()) {
            return false; // Corso non trovato
        }
        Corso corso = corsoOpt.get();
        corso.setActive(false);
        corso.setDocenti(null);
        List<CalendarioCorso> calendarioCorso = calendarioCorsoRepository.findByCorsoId(idCorso);
        for (CalendarioCorso calendario : calendarioCorso) {
            calendario.setActive(false);
        }
        corsoRepository.save(corso);
        return true;
    }

    @Transactional
    public boolean aggiungiCorsiBaseRuolo(HttpServletRequest request, HttpServletResponse response, Model model) {
        Optional<Socio> socioOpt = socioService.setSocioFromCookie(request, response, model);
        if (!socioOpt.isPresent()) {
            // Gestire il caso in cui il socio non è trovato
            return false;
        }
        Socio socio = socioOpt.get();
        List<Corso> corsi;
        if (socio.getDocente() != null) {
            // Il socio è un docente, quindi recupera i corsi insegnati da lui
            corsi = corsoRepository.findCorsiByDocenteId(socio.getDocente().getId()); //solo attivi
        } else if (socio.getSegretario() != null) {
            // Il socio è un segretario, quindi recupera tutti i corsi
            corsi = corsoRepository.findAllIfActiveTrue();
            System.out.println("Segretario");
        } else {
            // Gestire il caso in cui il socio non è né un docente né un segretario
            System.out.println("Errore identificativo socio");
            return false;
        }

        model.addAttribute("corsi", corsi);

        return true;
    }

    @Transactional
    List<Corso> findAllIfActiveTrue(){
        return corsoRepository.findAllIfActiveTrue();
    }

    @Transactional
    List<Corso> findCorsiByDocenteId(Integer docenteId){
        return corsoRepository.findCorsiByDocenteId(docenteId);
    }

}
