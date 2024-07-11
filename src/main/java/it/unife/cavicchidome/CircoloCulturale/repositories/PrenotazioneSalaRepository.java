package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.PrenotazioneSala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface PrenotazioneSalaRepository extends JpaRepository<PrenotazioneSala, Integer> {

    @Query("SELECT ps FROM PrenotazioneSala ps WHERE ps.idSala.id = :idSala AND ps.deleted = false AND ps.data = :data")
    List<PrenotazioneSala> findBySalaAndData(Integer idSala, LocalDate data);

    @Query("SELECT ps FROM PrenotazioneSala ps WHERE ps.idSocio.id = :idSocio AND ps.deleted = false")
    List<PrenotazioneSala> findBySocio(Integer idSocio);

    @Query("SELECT p FROM PrenotazioneSala p WHERE p.data = :date AND " +
            "p.idSala.id = :salaId AND " +
            "((:orarioInizio BETWEEN p.orarioInizio AND p.orarioFine) OR " +
            "(:orarioFine BETWEEN p.orarioInizio AND p.orarioFine) OR " +
            "(:orarioInizio <= p.orarioInizio AND :orarioFine >= p.orarioFine))")
    Optional<PrenotazioneSala> findOverlapPrenotazione(Integer salaId, LocalDate date, LocalTime orarioInizio, LocalTime orarioFine);
}