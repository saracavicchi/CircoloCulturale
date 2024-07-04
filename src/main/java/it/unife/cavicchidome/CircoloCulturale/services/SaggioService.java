package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.Biglietto;
import it.unife.cavicchidome.CircoloCulturale.models.Saggio;
import it.unife.cavicchidome.CircoloCulturale.repositories.SaggioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SaggioService {

    SaggioRepository saggioRepository;

    SaggioService(SaggioRepository saggioRepository) {
        this.saggioRepository = saggioRepository;
    }
    
    @Transactional
    public List<Saggio> getNextMonth() {
        LocalDate untilDate = LocalDate.now().plusDays(60);
        return saggioRepository.getNextSaggi(untilDate);
    }

    @Transactional
    public Optional<Saggio> findSaggioById(Integer saggioId) {
        return saggioRepository.findById(saggioId);
    }

    @Transactional
    public List<Saggio> findAllSaggi () {
        return saggioRepository.findAll();
    }

    public int getAvailableTickets(Saggio saggio) {
        int maxAvailable = saggio.getMaxPartecipanti();
        int confirmedTickets = 0;
        for (Biglietto b : saggio.getBiglietti()) {
            if (!b.getDeleted() && b.getStatoPagamento() == 'c') {
                confirmedTickets += b.getQuantita();
            }
        }
        return maxAvailable - confirmedTickets;
    }

}
