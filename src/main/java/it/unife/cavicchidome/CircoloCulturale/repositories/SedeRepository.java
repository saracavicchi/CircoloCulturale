package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SedeRepository extends JpaRepository<Sede, Integer> {
    @Query("SELECT s FROM Sede s JOIN s.sale sala WHERE sala.id = :idSala")
    Optional<Sede> findSedeByIdSala(@Param("idSala") Integer idSala);
}