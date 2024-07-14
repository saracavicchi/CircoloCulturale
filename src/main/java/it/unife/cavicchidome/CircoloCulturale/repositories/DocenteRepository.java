package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocenteRepository extends JpaRepository<Docente, Integer> {
    @Query("SELECT d.id, d.stipendio FROM Docente d JOIN d.corsi c WHERE c.id = :corsoId")
    List<Object[]> findDocentiByCorsoId(Integer corsoId);

    @Query("SELECT d FROM Docente d JOIN d.socio s JOIN s.utente u WHERE u.cf = :cf")
    Optional<Docente> findByCf(String cf);

    @Query("SELECT d FROM Docente d WHERE d.active = true AND d.socio.deleted = false AND d.socio.utente.deleted = false")
    List<Docente> findAll();


}