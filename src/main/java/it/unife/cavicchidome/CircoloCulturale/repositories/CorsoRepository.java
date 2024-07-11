package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Corso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CorsoRepository extends JpaRepository<Corso, Integer> {
    @Query("SELECT c FROM Corso c JOIN c.docenti d WHERE d.id = :docenteId AND c.active = true")
    List<Corso> findCorsiByDocenteId(Integer docenteId); // Query che restituisce i corsi (attivi)insegnati da un docente

    @Query("SELECT c FROM Corso c WHERE c.active = true")
    List<Corso> findAllIfActiveTrue(); // Query che restituisce tutti i corsi attivi
}