package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SocioService {

    SocioRepository socioRepository;

    SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Transactional
    public Optional<Integer> authenticate(String cf, String password) {
        Optional<Socio> socio = socioRepository.authenticateSocio(cf, password);
        if (socio.isPresent()) {
            return Optional.of(socio.get().getId());
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Socio> findSocioById(int id) {
        return socioRepository.findById(id);
    }
}
