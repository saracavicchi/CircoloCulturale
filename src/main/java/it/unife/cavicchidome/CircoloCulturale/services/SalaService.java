package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.Sala;
import it.unife.cavicchidome.CircoloCulturale.models.Sede;
import it.unife.cavicchidome.CircoloCulturale.models.Weekday;
import it.unife.cavicchidome.CircoloCulturale.repositories.SalaRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.SedeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class SalaService {
    private final SalaRepository salaRepository;
    private final SedeRepository sedeRepository;

    public SalaService(SalaRepository salaRepository, SedeRepository sedeRepository) {
        this.salaRepository = salaRepository;
        this.sedeRepository = sedeRepository;
    }

    @Transactional(readOnly = true)
    public List<Sala> findAll() {
        return salaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Sala> findAllPrenotabili() {
        return salaRepository.findAllPrenotabili();
    }

    @Transactional(readOnly = true)
    public Optional<Sala> findById(Integer idSala) {
        return salaRepository.findById(idSala);
    }


}
