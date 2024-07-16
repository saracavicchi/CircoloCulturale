package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.*;
import it.unife.cavicchidome.CircoloCulturale.repositories.*;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SedeService {

    private final SalaRepository salaRepository;
    private final SedeRepository sedeRepository;
    private final PrenotazioneSalaRepository prenotazioneSalaRepository;
    private final OrarioSedeRepository orarioSedeRepository;
    private final CorsoRepository corsoRepository;
    private final SegretarioRepository segretarioRepository;
    private final SocioRepository socioRepository;

    SedeService(SedeRepository sedeRepository, SalaRepository salaRepository, PrenotazioneSalaRepository prenotazioneSalaRepository, OrarioSedeRepository orarioSedeRepository, CorsoRepository corsoRepository, SegretarioRepository segretarioRepository, SocioRepository socioRepository) {
        this.sedeRepository = sedeRepository;
        this.salaRepository = salaRepository;
        this.prenotazioneSalaRepository = prenotazioneSalaRepository;
        this.orarioSedeRepository = orarioSedeRepository;
        this.corsoRepository = corsoRepository;
        this.segretarioRepository = segretarioRepository;
        this.socioRepository = socioRepository;
    }

    @Transactional
    public List<Sede> getAllSediActive() {
        return sedeRepository.findAllActive();
    }

    @Transactional
    public Optional<Sede> findSedeByIdActive(Integer id) {
        return sedeRepository.findByIdActive(id);
    }

    @Transactional
    public Optional<Sede> findSedeByIdAll(Integer id) {
        return sedeRepository.findByIdAll(id);
    }

    @Transactional
    public Optional<Sede> findSedeByIdSalaActive(Integer idSala) {
        return sedeRepository.findSedeByIdSalaActive(idSala);
    }

    @Transactional
    public Optional<Sede> sedeAvailableDate(Integer idSede, LocalDate date) {
        return sedeRepository.findAvailableSedeDate(idSede, date, Weekday.fromDayNumber(date.getDayOfWeek().getValue()));
    }

    @Transactional
    public OrarioSede findOrarioSede(Integer idSede, Weekday dow) {
        return sedeRepository.findOrarioSede(idSede, dow);
    }

    @Transactional
    public boolean outsideOpeningHours(Integer idSala, Weekday dow, LocalTime startTime, LocalTime endTime) {
        Sede sede = sedeRepository.getReferenceById(salaRepository.getReferenceById(idSala).getIdSede().getId());
        OrarioSede orarioSede = findOrarioSede(sede.getId(), dow);
        if (startTime.isBefore(orarioSede.getOrarioApertura()) || endTime.isAfter(orarioSede.getOrarioChiusura())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validateSedeInfo(String nome, String stato, String provincia, String citta, String via, String numeroCivico, List<LocalDate> chiusura, List<LocalTime> openingTimes, List<LocalTime> closingTimes) {

        if(!validateNome(nome) || !validateIndirizzo(stato, provincia, citta, via, numeroCivico) || !validateOpeningClosingTimes(openingTimes, closingTimes) ){
            return false;
        }
        if(chiusura != null && !validateChiusura(chiusura)){
            return false;
        }

        return true;
    }

    private boolean validateChiusura(List<LocalDate> chiusura) {
        LocalDate now = LocalDate.now();
        LocalDate oneYearFromNow = now.plusYears(1);

        if (chiusura.isEmpty()) {
            return true;
        }
        for (LocalDate date : chiusura) {
            if (date.isBefore(now) || date.isAfter(oneYearFromNow)) {
                return false;
            }
        }

        return true;
    }

    private boolean validateNome(String nome) {
        String regex = "^(?=.*\\p{L})[\\p{L}\\s\\-]+$";
        int maxLengthNome = 30;

        if (nome == null) {
            return false;
        }
        if (!nome.matches(regex)) {
            return false;
        }
        if (nome.length() > maxLengthNome) {
            return false;
        }

        return true;
    }

    private boolean validateIndirizzo(String stato, String provincia, String citta, String via, String numeroCivico) {
        String regex = "^(?=.*\\p{L})[\\p{L}\\s\\-]+$";
        int maxLengthTotal = 80;

        if (stato == null || provincia == null || citta == null || via == null || numeroCivico == null) {
            return false;
        }
        if (!stato.matches(regex) || !provincia.matches(regex) || !citta.matches(regex) || !via.matches(regex)) {
            return false;
        }
        
        String houseNumberRegex = "^(?=.*[0-9])[0-9a-zA-Z/]+$";;
        if (numeroCivico.isEmpty() || numeroCivico== null || !numeroCivico.matches(houseNumberRegex)) {
            return false;
        }

        int totalLength = stato.length() + provincia.length() + citta.length() + via.length() + numeroCivico.length();
        if (totalLength > maxLengthTotal) {
            return false;
        }

        return true;
    }
    private boolean validateOpeningClosingTimes(List<LocalTime> openingTimes, List<LocalTime> closingTimes) {
        if (openingTimes.size() != 7 || closingTimes.size() != 7) {
            return false;
        }
        for (int i = 0; i < 7; i++) {
            LocalTime opening = openingTimes.get(i);
            LocalTime closing = closingTimes.get(i);

            if (!opening.isBefore(closing)) {
                return false;
            }
        }

        return true;
    }

    public boolean validateStipendio(Integer stipendio){
        if (stipendio == null ) return false;

        return true;
    }

    @Transactional
    public boolean newSede(String nome, String stato, String provincia, String citta, String via, String numeroCivico, boolean areaRistoro, List<LocalDate> chiusura, List<LocalTime> openingTimes, List<LocalTime> closingTimes, Integer idSegretario, boolean admin) {
        // Valida parametri
        if (!validateSedeInfo(nome, stato, provincia, citta, via, numeroCivico, chiusura, openingTimes, closingTimes)) {
            return false;
        }
        String indirizzo = stato + ", " + provincia + ", " + citta + ", " + via + ", " + numeroCivico;
        if (sedeRepository.findSedeByIndirizzoActive(indirizzo).isPresent()) {
            throw new RuntimeException("Indirizzo già presente");
        }

        if (sedeRepository.findSedeByNomeActive(nome).isPresent()) {
            throw new RuntimeException("Nome già presente");
        }

        // Crea nuova sede
        Sede newSede = new Sede();
        newSede.setNome(nome);
        newSede.setIndirizzo(indirizzo);
        newSede.setRistoro(areaRistoro);
        newSede.setActive(true);
        if(chiusura != null){
            Set<LocalDate> giorniChiusura = new HashSet<>(chiusura);
            newSede.setGiornoChiusura(giorniChiusura);
        }

        Optional<Socio> socioOpt = socioRepository.findById(idSegretario); //solo soci active
        if (!socioOpt.isPresent()) {
            return false;
        }
        Segretario segretario;
        Optional<Segretario> segretarioOpt = segretarioRepository.findByIdEvenIfNotActive(idSegretario);
        if(segretarioOpt.isPresent()){
            if(segretarioOpt.get().getActive() == true){
                throw new RuntimeException("Segretario già presente");
            }
            else {
                segretario = segretarioOpt.get();
                segretario.setActive(true);
            }
        }
        else{
            segretario = new Segretario();
            segretario.setActive(true);

        }
        segretario.setSocio(socioOpt.get());
        segretario.setAdmin(admin);
        if(admin){
            segretario.setStipendio(new BigDecimal(70000*1.1)); //TODO: ingegnerizzare meglio
        }
        else{
            segretario.setStipendio(new BigDecimal(70000));
        }
        segretario.setSedeAmministrata(newSede);
        segretarioRepository.save(segretario);

        sedeRepository.save(newSede);
        sedeRepository.flush();
        System.out.println("newSedeId: " + newSede.getId());


        Set<OrarioSede> orarisede = new HashSet<>();
        for(int i = 0; i < openingTimes.size(); i++){
            Weekday weekday = Weekday.fromDayNumber(i+1); //1: monday...
            OrarioSede orarioSede = new OrarioSede();
            OrarioSedeId orarioSedeId = new OrarioSedeId();
            orarioSedeId.setGiornoSettimana(weekday);
            orarioSedeId.setIdSede(newSede.getId());
            orarioSede.setId(orarioSedeId);
            orarioSede.setIdSede(newSede);
            orarioSede.setOrarioApertura(openingTimes.get(i));
            orarioSede.setOrarioChiusura(closingTimes.get(i));
            orarioSedeRepository.save(orarioSede);
            orarisede.add(orarioSede);
        }
        newSede.setOrarioSede(orarisede);


        // Salva nuova sede nel database
        sedeRepository.save(newSede);
        return true;
    }

    @Transactional
    public boolean updateSede(Integer idSede, String nome, boolean areaRistoro, List<LocalDate> chiusura, List<LocalDate> deletedChiusura, boolean admin, Integer idSegretario, boolean adminNuovo) {
        BigDecimal baseStipendio = new BigDecimal(70000);
        BigDecimal adminBonus = new BigDecimal(1.1);

        if(!validateNome(nome)){
            return false;
        }
        if(chiusura != null && !validateChiusura(chiusura)){
            return false;
        }
        try {
            Optional<Sede> sedeOpt = sedeRepository.findByIdActive(idSede);
            if (!sedeOpt.isPresent()) {
                return false;
            }
            Optional<Sede> sedeOptNome = sedeRepository.findSedeByNomeActive(nome);
            if (sedeOptNome.isPresent() && !sedeOptNome.get().getId().equals(idSede)) {
                throw new RuntimeException("Nome già presente");
            }
            Sede sede = sedeOpt.get();

            if(chiusura != null && checkIfGiornoChiusuraAlreadyPresent(sede, chiusura)){
                throw new RuntimeException("Giorno chiusura già presente");
            }
            if(chiusura!= null && chiusura.isEmpty() == false){
                for(LocalDate date : chiusura) {  //cancellazione di tutte le prenotazioni in quel giorno
                    List<PrenotazioneSala> prenotazioni = prenotazioneSalaRepository.findByDateAndSede(date, sede);
                    if (!prenotazioni.isEmpty()) {
                        for (PrenotazioneSala prenotazione : prenotazioni) {
                            prenotazione.setDeleted(true);
                            prenotazioneSalaRepository.save(prenotazione);
                        }
                    }
                }
                sede.getGiornoChiusura().addAll(chiusura);
                sede.setGiornoChiusura(sede.getGiornoChiusura());
            }

            if(deletedChiusura != null && deletedChiusura.isEmpty() == false){
                for(LocalDate date : deletedChiusura){
                    sede.getGiornoChiusura().remove(date);
                }


            }
            Segretario segretario;
            if(idSegretario != 0) {//significa che è stato selezionato un nuovo segretario
                Optional<Socio> socioOpt = socioRepository.findById(idSegretario); //solo soci active
                if (!socioOpt.isPresent()) {
                    return false;
                }

                Optional<Segretario> segretarioOpt = segretarioRepository.findByIdEvenIfNotActive(idSegretario);
                if (segretarioOpt.isPresent()) {
                    if (segretarioOpt.get().getActive() == true) {
                        throw new RuntimeException("Segretario già presente");
                    } else {
                        segretario = segretarioOpt.get();
                    }
                } else {
                    segretario = new Segretario();
                    segretario.setSocio(socioOpt.get());

                }
                segretario.setSocio(socioOpt.get());
                segretario.setActive(true);
                Segretario oldSegretario = sede.getSegretario();
                oldSegretario.setActive(false);
                segretario.setAdmin(adminNuovo);
                BigDecimal stipendio = adminNuovo ? baseStipendio.multiply(adminBonus) : baseStipendio;
                segretario.setStipendio(stipendio);
                segretarioRepository.save(segretario);
                segretarioRepository.save(oldSegretario);
                sede.setSegretario(segretario);
                sedeRepository.save(sede);

            }
            else{
                segretario = sede.getSegretario();
                segretario.setAdmin(admin);
                BigDecimal stipendio = admin ? baseStipendio.multiply(adminBonus) : baseStipendio;
                segretario.setStipendio(stipendio);
                sede.setSegretario(segretario);
                sedeRepository.save(sede);

            }

            segretario.setSedeAmministrata(sede);
            segretarioRepository.save(segretario);


            sede.setNome(nome);
            sede.setRistoro(areaRistoro);

            sedeRepository.save(sede);
            return true;
        } catch (Exception e) {
            // Log the exception
            System.out.println("Error updating Sede: " + e.getMessage());
            return false;
        }
    }

    public boolean checkIfGiornoChiusuraAlreadyPresent(Sede sede, List<LocalDate> chiusura) {
        for(LocalDate date : chiusura){
            if(sede.getGiornoChiusura().contains(date)){
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean deleteSede(Integer idSede) {
        try {
            Optional<Sede> sedeOpt = sedeRepository.findById(idSede);
            if (!sedeOpt.isPresent()) {
                return false;
            }
            Sede sede = sedeOpt.get();
            List<Sala> sale = salaRepository.findAllActiveBySedeId(idSede);
            if(!sale.isEmpty()){
                for(Sala sala : sale){
                    sala.setActive(false);
                    salaRepository.save(sala);
                }
            }
            List<Corso> corsi = corsoRepository.findBySedeId(idSede); //tutto active
            if(!corsi.isEmpty()){
                for(Corso corso : corsi){
                    Set<CalendarioCorso> calendarioCorsoSet = corso.getCalendarioCorso();
                    for(CalendarioCorso calendarioCorso : calendarioCorsoSet){
                        calendarioCorso.setActive(false);
                    }
                    corso.setCalendarioCorso(null);
                    corso.setActive(false);
                    corsoRepository.save(corso);
                }
            }
            List<PrenotazioneSala> prenotazioni = prenotazioneSalaRepository.findBySedeId(idSede); //Solo active
            if (!prenotazioni.isEmpty()) {
                for (PrenotazioneSala prenotazione : prenotazioni) {
                    prenotazione.setDeleted(true);
                    prenotazioneSalaRepository.save(prenotazione);
                }
            }

            Segretario segretario = sede.getSegretario();
            segretario.setActive(false);
            segretarioRepository.save(segretario);
            sede.setActive(false);
            sedeRepository.save(sede);

            return true;
        } catch (Exception e) {
            // Log the exception
            System.out.println("Error deleting Sede: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public Optional<Sede> findSedeByNomeActive(String nome) {
        return sedeRepository.findSedeByNomeActive(nome);
    }

    @Transactional
    public Optional<Sede> findSedeByNomeAll(String nome) {
        return sedeRepository.findSedeByNomeAll(nome);
    }

    @Transactional
    public Optional<Sede> findSedeByIndirizzoActive(String indirizzo) {
        return sedeRepository.findSedeByIndirizzoActive(indirizzo);
    }

    @Transactional
    public Optional<Sede> findSedeByIndirizzoAll(String indirizzo) {
        return sedeRepository.findSedeByIndirizzoAll(indirizzo);
    }

    @Transactional
    List<Sede> findAllEvenIfNotActive(){
        return sedeRepository.findAllEvenIfNotActive();
    }

    @Transactional
    public Optional<Sede> findActiveSedeWithMinId() {
        return sedeRepository.findActiveSedeWithMinId();
    }
}
