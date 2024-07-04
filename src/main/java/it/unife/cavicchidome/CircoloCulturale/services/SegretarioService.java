package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.unife.cavicchidome.CircoloCulturale.models.Segretario;
import it.unife.cavicchidome.CircoloCulturale.repositories.SegretarioRepository;

import java.util.Optional;

@Service
public class SegretarioService {

    SegretarioRepository segretarioRepository;
    private final String commonPassword;

    public SegretarioService(SegretarioRepository segretarioRepository, @Value("${segretario.common.password}") String commonPassword) {
        this.segretarioRepository = segretarioRepository;
        this.commonPassword = commonPassword;
    }

    @Transactional
    public Optional<Segretario> findById(int id) {
        return segretarioRepository.findById(id);
    }




    public boolean validateCommonPassword(String inputPassword) {
        return this.commonPassword.equals(inputPassword);
    }
}
