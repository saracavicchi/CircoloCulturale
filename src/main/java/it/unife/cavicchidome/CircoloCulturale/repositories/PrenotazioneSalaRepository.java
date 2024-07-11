package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.PrenotazioneSala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrenotazioneSalaRepository extends JpaRepository<PrenotazioneSala, Integer> {

    @Query("SELECT ps FROM PrenotazioneSala ps WHERE ps.idSala.id = :idSala AND ps.deleted = false AND ps.data = :data")
    List<PrenotazioneSala> findBySalaAndData(Integer idSala, String data);

    @Query("SELECT ps FROM PrenotazioneSala ps WHERE ps.idSocio.id = :idSocio AND ps.deleted = false")
    List<PrenotazioneSala> findBySocio(Integer idSocio);
}