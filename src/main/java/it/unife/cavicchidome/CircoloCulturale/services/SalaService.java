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

    public boolean validateSalaInfo(String numeroSala, Integer capienza) {
        // Controlla se numeroSala o capienza sono nulli
        if (numeroSala == null || capienza == null) {
            return false;
        }

        // Controlla se numeroSala è un numero valido (non negativo)
        if (Integer.parseInt(numeroSala) < 0) {
            return false;
        }

        // Controlla se capienza è un intero positivo
        if (capienza <= 0) {
            return false;
        }

        // Se tutte le condizioni sono soddisfatte, ritorna true
        return true;
    }

    public boolean newSala(String numeroSala, Integer capienza, String descrizione, Boolean prenotabile, Integer idSede) {
        if(!validateSalaInfo(numeroSala, capienza)) {
            return false;
        }

        // Find the Sede by idSede
        Optional<Sede> sedeOptional = sedeRepository.findById(idSede);
        if (!sedeOptional.isPresent()) {
            throw new IllegalArgumentException("Sede with the provided ID does not exist.");
        }
        if(salaRepository.findByNumeroSalaAndIdSede(Integer.parseInt(numeroSala), sedeOptional.get()).isPresent())
            throw new IllegalArgumentException("Sala già presente");

        // Step 3: Create a new Sala instance
        Sala newSala = new Sala();
        newSala.setNumeroSala(Integer.parseInt(numeroSala));
        newSala.setCapienza(capienza);
        if(descrizione != null && !descrizione.isEmpty())
            newSala.setDescrizione(descrizione);
        newSala.setPrenotabile(prenotabile);
        newSala.setActive(true);
        newSala.setIdSede(sedeOptional.get());

        // Save the Sala instance to the repository
        Sala savedSala = salaRepository.save(newSala);


        return true;
    }


}
