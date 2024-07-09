package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.Sede;
import it.unife.cavicchidome.CircoloCulturale.repositories.SedeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SedeService {

    SedeRepository sedeRepository;

    SedeService(SedeRepository sedeRepository) {
        this.sedeRepository = sedeRepository;
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
}
