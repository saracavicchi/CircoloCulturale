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
}
