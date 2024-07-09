package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SalaRepository extends JpaRepository<Sala, Integer> {
    Optional<Sala>  findById(Integer idSala);
    List<Sala> findAll();
}