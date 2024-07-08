package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocenteRepository extends JpaRepository<Docente, Integer> {
    @Query("SELECT d.id, d.stipendio FROM Docente d JOIN d.corsi c WHERE c.id = :corsoId")
    List<Object[]> findDocentiByCorsoId(Integer corsoId);


}