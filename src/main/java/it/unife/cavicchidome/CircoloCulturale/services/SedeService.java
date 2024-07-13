package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.OrarioSede;
import it.unife.cavicchidome.CircoloCulturale.models.Sede;
import it.unife.cavicchidome.CircoloCulturale.models.Weekday;
import it.unife.cavicchidome.CircoloCulturale.repositories.SalaRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.SedeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class SedeService {

    private final SalaRepository salaRepository;
    SedeRepository sedeRepository;

    SedeService(SedeRepository sedeRepository, SalaRepository salaRepository) {
        this.sedeRepository = sedeRepository;
        this.salaRepository = salaRepository;
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

    public boolean validateSedeInfo(String nome, String stato, String provincia, String citta, String via, String numeroCivico) {

        if(!validateNome(nome) || !validateIndirizzo(stato, provincia, citta, via, numeroCivico)){
            return false;
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
    @Transactional
    public boolean newSede(String nome, String stato, String provincia, String citta, String via, String numeroCivico, boolean areaRistoro) {
        // Valida parametri
        if (!validateSedeInfo(nome, stato, provincia, citta, via, numeroCivico)) {
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


        // Salva nuova sede nel database
        sedeRepository.save(newSede);
        return true;
    }

    @Transactional
    public boolean updateSede(Integer idSede, String nome, boolean areaRistoro) {
        if(!validateNome(nome)){
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
}
