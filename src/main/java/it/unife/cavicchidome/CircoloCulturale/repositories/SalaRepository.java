package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface SalaRepository extends JpaRepository<Sala, Integer> {
    Optional<Sala>  findById(Integer idSala);
    List<Sala> findAll();

    @Query("SELECT s FROM Sala s WHERE s.active = true")
    List<Sala> findAllIfActive();
}