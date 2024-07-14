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


    //public SegretarioService(SegretarioRepository segretarioRepository, @Value("${segretario.common.password}") String commonPassword) {
    public SegretarioService(SegretarioRepository segretarioRepository) {
        this.segretarioRepository = segretarioRepository;
    }

    @Transactional
    public Optional<Segretario> findById(Integer id) {
        return segretarioRepository.findById(id);
    }

    @Transactional
    public Optional<Segretario> findByIdEvenIfNotActive(Integer id) {
        return segretarioRepository.findByIdEvenIfNotActive(id);
    }




    //public boolean validateCommonPassword(String inputPassword) {
       // return this.commonPassword.equals(inputPassword);
    //}
}
