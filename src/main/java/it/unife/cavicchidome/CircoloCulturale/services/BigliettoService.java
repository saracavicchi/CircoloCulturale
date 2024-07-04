package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.Biglietto;
import it.unife.cavicchidome.CircoloCulturale.repositories.BigliettoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BigliettoService {

    private final BigliettoRepository bigliettoRepository;

    public BigliettoService(BigliettoRepository bigliettoRepository) {
        this.bigliettoRepository = bigliettoRepository;
    }

    @Transactional
    public Integer saveBiglietto(Biglietto biglietto) {
        return bigliettoRepository.save(biglietto).getId();
    }
}
