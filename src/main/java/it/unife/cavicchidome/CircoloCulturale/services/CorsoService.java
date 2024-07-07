package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.*;
import it.unife.cavicchidome.CircoloCulturale.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            SedeService sedeService
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

        // Regular expression to check for letters and hyphens
        String regex = "^[a-zA-Z- ]+$";

        if (descrizione == null || !descrizione.matches(regex)) return false;
        if (genere == null || !genere.matches(regex) || genere.length() > 20) return false;
        if (livello == null || !livello.matches(regex) || livello.length() > 20) return false;
        if (categoria == null || categoria.isEmpty()) return false;
        if (idSala == null) return false;
        if (docentiCf == null || docentiCf.isEmpty()) return false;
        if (giorni == null || giorni.isEmpty()) return false;
        if (orarioInizio == null || orarioInizio.isEmpty() || orarioFine == null || orarioFine.isEmpty()) return false;
        if (orarioInizio.size() != orarioFine.size()) return false; // Ensure lists are of equal size

        for (int i = 0; i < giorni.size(); i++) {
            Integer giorno = giorni.get(i);
            List<LocalTime> orariAperturaChiusura = orarioSedeService.findOrarioAperturaChiusuraByIdSedeAndGiornoSettimana(sedeService.findSedeByIdSala(idSala).get().getId(), Weekday.values()[giorno-1]);
            LocalTime orarioApertura = orariAperturaChiusura.get(0);
            LocalTime orarioChiusura = orariAperturaChiusura.get(1);
            LocalTime inizio = orarioInizio.get(i);
            LocalTime fine = orarioFine.get(i);

            if (orarioInizio.get(i) == null || orarioFine.get(i) == null || !orarioInizio.get(i).isBefore(orarioFine.get(i))) {
                return false;
            }

            if (inizio.isBefore(orarioApertura) || fine.isAfter(orarioChiusura)) {
                return false;
            }
        }

        for (int i = 0; i < stipendi.size(); i++) {
            if (stipendi.get(i) == null || (stipendi.get(i) < 10000 || stipendi.get(i) > 100000)) {
                return false;
            }
        }

        return true;
    }

    @Transactional(readOnly = true)
    public Corso findCorsoByCategoriaGenereLivello(String categoria, String genere, String livello) {
        return corsoRepository.findByCategoriaAndGenereAndLivello(categoria, genere, livello).orElse(null);
    }

    // Assuming the existence of necessary repositories and their methods
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
        // Step 1: Check for existing course
        if (corsoRepository.findByCategoriaAndGenereAndLivello(categoria, genere, livello).isPresent()) {
            return false; // Course already exists
        }

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
                if (BigDecimal.valueOf(stipendi.get(i)).compareTo(socio.getDocente().getStipendio()) > 0)
                    socio.getDocente().setStipendio(BigDecimal.valueOf(stipendi.get(i)));
                docenti.add(socio.getDocente());
            }

        }
        //Controllo sovrapposizione oraria di corsi che si tengono nella stessa sala
        boolean sovrapposizione = false;
        for (int i = 0; i < giorni.size(); i++) {
            Integer giorno = giorni.get(i);
            LocalTime inizio = orarioInizio.get(i);
            LocalTime fine = orarioFine.get(i);

            // Trova corsi nel CalendarioCorso che si sovrappongono per orario
            List<CalendarioCorso> corsiSovrapposti = calendarioCorsoRepository.findCorsiSovrapposti(Weekday.values()[giorno] , inizio, fine);
            for (CalendarioCorso calendarioCorso : corsiSovrapposti) {
                Corso corsoSovrapposto = calendarioCorso.getIdCorso();
                if (corsoSovrapposto.getIdSala().equals(idSala)) {
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



        // Step 6: Save Course Information
        Corso corso = new Corso();
        corso.setDescrizione(descrizione);
        corso.setGenere(genere);
        corso.setLivello(livello);
        corso.setCategoria(categoria);
        corso.setIdSala(sala);
        corso.setDocenti(docenti);
        corso.setActive(true);

        if(photo != null){
            String filename = saveCorsoPicture(photo, categoria, corso.getId());
            corso.setUrlFoto(filename);
        }
        corsoRepository.save(corso);

        for (Integer giorno: giorni) {
            CalendarioCorso calendarioCorso = new CalendarioCorso();
            CalendarioCorsoId calendarioCorsoId = new CalendarioCorsoId();
            calendarioCorsoId.setIdCorso(corso.getId()); // Assuming getId() method exists in Corso
            calendarioCorsoId.setGiornoSettimana(Weekday.values()[giorno] );

            calendarioCorso.setId(calendarioCorsoId);
            calendarioCorso.setIdCorso(corso); // Set the Corso object
            calendarioCorso.setOrarioInizio(orarioInizio.get(giorno-1));
            calendarioCorso.setOrarioFine(orarioFine.get(giorno-1));
            calendarioCorsoRepository.save(calendarioCorso);
        }








        return true;
    }

    public String saveCorsoPicture (MultipartFile picture, String categoria, Integer idCorso) {

        if (picture == null || picture.isEmpty()) {
            return null;
        }

        String originalFilename = picture.getOriginalFilename();
        if (originalFilename == null) {
            return null;
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        String filename = categoria + idCorso + extension;

        try {
            Path picturePath = Paths.get(uploadCorsoDir, filename);
            picture.transferTo(picturePath);
            return filename;
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            return null;
        }
    }

    @Transactional
    public List<Corso> filterCorsi(Optional<String> category,
                                   Optional<String> genre,
                                   Optional<String> level,
                                   Optional<Integer> socioId) {
        List<Corso> corsi = corsoRepository.findAll();

        if (category.isPresent()) {
            List<Corso> categoryFilteredCorsi = new ArrayList<>();
            for (Corso c : corsi) {
                if (c.getCategoria().equals(category.get())) {
                    categoryFilteredCorsi.add(c);
                }
            }
            corsi = categoryFilteredCorsi;
        }

        if (genre.isPresent()) {
            List<Corso> genreFilteredCorsi = new ArrayList<>();
            for (Corso c : corsi) {
                if (c.getGenere().equals(genre.get())) {
                    genreFilteredCorsi.add(c);
                }
            }
            corsi = genreFilteredCorsi;
        }

        if (level.isPresent()) {
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
    public Optional<Corso> findById(Integer idCorso) {
        return corsoRepository.findById(idCorso);
    }
}
