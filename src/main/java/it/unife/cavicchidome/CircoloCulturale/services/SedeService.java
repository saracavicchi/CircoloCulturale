package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.*;
import it.unife.cavicchidome.CircoloCulturale.repositories.OrarioSedeRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.PrenotazioneSalaRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.SalaRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.SedeRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    SedeService(SedeRepository sedeRepository, SalaRepository salaRepository, PrenotazioneSalaRepository prenotazioneSalaRepository, OrarioSedeRepository orarioSedeRepository) {
        this.sedeRepository = sedeRepository;
        this.salaRepository = salaRepository;
        this.prenotazioneSalaRepository = prenotazioneSalaRepository;
        this.orarioSedeRepository = orarioSedeRepository;
    }

    @Transactional
    public List<Sede> getAllSedi() {
        return sedeRepository.findAll();
    }

    @Transactional
    public Optional<Sede> findSedeById(Integer id) {
        return sedeRepository.findById(id);
    }

    @Transactional
    public Optional<Sede> findSedeByIdSala(Integer idSala) {
        return sedeRepository.findSedeByIdSala(idSala);
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

    public boolean validateSedeInfo(String nome, String stato, String provincia, String citta, String via, String numeroCivico, List<LocalDate> chiusura, List<LocalTime> openingTimes, List<LocalTime> closingTimes) {

        if(!validateNome(nome) || !validateIndirizzo(stato, provincia, citta, via, numeroCivico) || !validateOpeningClosingTimes(openingTimes, closingTimes)){
            return false;
        }
        if(chiusura != null && !validateChiusura(chiusura)){
            return false;
        }

        return true;
    }

    public boolean validateChiusura(List<LocalDate> chiusura) {
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

    public boolean validateNome(String nome) {
        String regex = "^[A-Za-z\\s\\-]+$";
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

    public boolean validateIndirizzo(String stato, String provincia, String citta, String via, String numeroCivico) {
        String regex = "^[A-Za-z\\s\\-]+$";
        int maxLengthTotal = 80;

        if (stato == null || provincia == null || citta == null || via == null || numeroCivico == null) {
            return false;
        }
        if (!stato.matches(regex) || !provincia.matches(regex) || !citta.matches(regex) || !via.matches(regex)) {
            return false;
        }

        int totalLength = stato.length() + provincia.length() + citta.length() + via.length() + numeroCivico.length();
        if (totalLength > maxLengthTotal) {
            return false;
        }

        return true;
    }
    public boolean validateOpeningClosingTimes(List<LocalTime> openingTimes, List<LocalTime> closingTimes) {
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

    @Transactional
    public boolean newSede(String nome, String stato, String provincia, String citta, String via, String numeroCivico, boolean areaRistoro, List<LocalDate> chiusura, List<LocalTime> openingTimes, List<LocalTime> closingTimes) {
        // Valida parametri
        if (!validateSedeInfo(nome, stato, provincia, citta, via, numeroCivico, chiusura, openingTimes, closingTimes)) {
            return false;
        }
        String indirizzo = stato + ", " + provincia + ", " + citta + ", " + via + ", " + numeroCivico;
        if (sedeRepository.findSedeByIndirizzo(indirizzo).isPresent()) {
            throw new RuntimeException("Indirizzo già presente");
        }
        if (sedeRepository.findSedeByNome(nome).isPresent()) {
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
    public boolean updateSede(Integer idSede, String nome, boolean areaRistoro, List<LocalDate> chiusura, List<LocalDate> deletedChiusura) {
        if(!validateNome(nome)){
            return false;
        }
        if(chiusura != null && !validateChiusura(chiusura)){
            return false;
        }
        try {
            Optional<Sede> sedeOpt = sedeRepository.findById(idSede);
            if (!sedeOpt.isPresent()) {
                return false;
            }
            Optional<Sede> sedeOptNome = sedeRepository.findSedeByNome(nome);
            if (sedeOptNome.isPresent() && !sedeOptNome.get().getId().equals(idSede)) {
                throw new RuntimeException("Nome già presente");
            }
            Sede sede = sedeOpt.get();

            if(chiusura != null && checkIfGiornoChiusuraAlreadyPresent(sede, chiusura)){
                throw new RuntimeException("Giorno chiusura già presente");
            }
            if(deletedChiusura != null && deletedChiusura.isEmpty() == false){
                for(LocalDate date : deletedChiusura){  //cancellazione di tutte le prenotazioni in quel giorno
                    List<PrenotazioneSala> prenotazioni = prenotazioneSalaRepository.findByDate(date);
                    if(!prenotazioni.isEmpty()){
                        for (PrenotazioneSala prenotazione : prenotazioni) {
                            prenotazione.setDeleted(true);
                        }
                    }
                    sede.getGiornoChiusura().remove(date);
                }
            }
            sede.getGiornoChiusura().addAll(chiusura);

            sede.setNome(nome);
            sede.setRistoro(areaRistoro);
            sede.setGiornoChiusura(sede.getGiornoChiusura());
            sedeRepository.save(sede);
            return true;
        } catch (Exception e) {
            // Log the exception
            System.out.println("Error updating Sede: " + e.getMessage());
            return false;
        }
    }

    boolean checkIfGiornoChiusuraAlreadyPresent(Sede sede, List<LocalDate> chiusura) {
        for(LocalDate date : chiusura){
            if(sede.getGiornoChiusura().contains(date)){
                return true;
            }
        }
        return false;
    }
}
