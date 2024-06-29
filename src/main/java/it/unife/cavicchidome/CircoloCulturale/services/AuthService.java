package it.unife.cavicchidome.CircoloCulturale.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    SocioRepository socioRepository;

    AuthService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Transactional
    public Socio authenticate(String username, String password) {
        return socioRepository.authenticateSocio(username,password);
    }
}
