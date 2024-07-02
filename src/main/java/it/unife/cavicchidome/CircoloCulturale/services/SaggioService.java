package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.entity.Biglietto;
import it.unife.cavicchidome.CircoloCulturale.models.entity.Saggio;
import it.unife.cavicchidome.CircoloCulturale.repositories.SaggioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
    
    public int getAvailableTickets(Saggio saggio) {
        int maxAvailable = saggio.getMaxPartecipanti();
        int confirmedTickets = 0;
        for (Biglietto b : saggio.getBiglietti()) {
            if (!b.getDeleted() && b.getStatoPagamento() == 'c') {
                confirmedTickets += b.getQuantita();
            }
        }
        return confirmedTickets;
    }

}
