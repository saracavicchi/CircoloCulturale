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
        // Regex per validare che il testo contenga solo caratteri, spazi o trattini
        String regex = "^[A-Za-z\\s\\-]+$";
        int maxLengthNome = 30;
        int maxLengthTotal = 80;

        // Controllo presenza e formato di nome, stato, provincia, citta, via
        if (nome == null || stato == null || provincia == null || citta == null || via == null || numeroCivico == null) {
            return false;
        }
        if (!nome.matches(regex) || !stato.matches(regex) || !provincia.matches(regex) || !citta.matches(regex) || !via.matches(regex)) {
            return false;
        }

        // Controllo lunghezza massima di nome
        if (nome.length() > maxLengthNome) {
            return false;
        }

        // Controllo lunghezza totale di stato, provincia, citta, via, numero civico
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
}
