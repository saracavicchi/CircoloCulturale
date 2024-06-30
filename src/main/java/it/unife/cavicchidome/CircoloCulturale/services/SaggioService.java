package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.repositories.SaggioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaggioService {

    SaggioRepository saggioRepository;

    SaggioService(SaggioRepository saggioRepository) {
        this.saggioRepository = saggioRepository;
    }

}
