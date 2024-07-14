package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Segretario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SegretarioRepository extends JpaRepository<Segretario, Integer> {

    @Query("SELECT s FROM Segretario s WHERE s.active = true AND s.socio.id = ?1 AND s.socio.deleted = false AND s.socio.utente.deleted = false")
    Optional<Segretario> findById(Integer id);

    @Query("SELECT s FROM Segretario s WHERE s.socio.id = ?1 AND s.socio.deleted = false AND s.socio.utente.deleted = false")
    Optional<Segretario> findByIdEvenIfNotActive(Integer id);
}