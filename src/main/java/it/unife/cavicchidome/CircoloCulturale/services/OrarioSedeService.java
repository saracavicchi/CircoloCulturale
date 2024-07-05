package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.OrarioSede;
import it.unife.cavicchidome.CircoloCulturale.models.Weekday;
import it.unife.cavicchidome.CircoloCulturale.repositories.OrarioSedeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrarioSedeService {
    private OrarioSedeRepository orarioSedeRepository;

    public OrarioSedeService(OrarioSedeRepository orarioSedeRepository) {
        this.orarioSedeRepository = orarioSedeRepository;
    }

    public List<LocalTime> findOrarioAperturaChiusuraByIdSedeAndGiornoSettimana(Integer idSede, Weekday giornoSettimana) {
        Optional<OrarioSede> orarioSede = orarioSedeRepository.findByIdSedeAndGiornoSettimana(idSede, giornoSettimana);
        if (orarioSede.isPresent()) {
            List<LocalTime> orari = new ArrayList<>();
            orari.add(orarioSede.get().getOrarioApertura());
            orari.add(orarioSede.get().getOrarioChiusura());
            return orari;
        } else {
            return new ArrayList<>();
        }
    }
}

