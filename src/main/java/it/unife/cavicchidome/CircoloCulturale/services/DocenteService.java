package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.Docente;
import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import it.unife.cavicchidome.CircoloCulturale.repositories.DocenteRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DocenteService {

    private final DocenteRepository docenteRepository;
    private final UtenteService utenteService;
    private final UtenteRepository utenteRepository;


    public DocenteService(
            DocenteRepository docenteRepository,
            UtenteService utenteService,
            UtenteRepository utenteRepository) {
        this.docenteRepository = docenteRepository;
        this.utenteService = utenteService;

        this.utenteRepository = utenteRepository;
    }

    @Transactional(readOnly = true)
    public List<Docente> findAll() {
        return docenteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public boolean existsDocenteWithCf(String cf) {
        Optional<Utente> utente = utenteRepository.findByCf(cf);
        if (!utente.isPresent()) {
            return false;
        }
        return docenteRepository.findById(utente.get().getId()).isPresent();
    }

    @Transactional
    public Optional<Docente> findById(Integer docenteId) {
        return docenteRepository.findById(docenteId);
    }
}
