package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Biglietto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BigliettoRepository extends JpaRepository<Biglietto, Integer> {

    @Query("SELECT b FROM Biglietto b WHERE b.idUtente.nome LIKE %:nome% AND b.idUtente.cognome LIKE %:cognome% AND b.idSaggio.id = :idSaggio AND (b.deleted = :deleted OR b.deleted = true)")
    List<Biglietto> findBigliettoNameSurnameSaggioDeleted(String nome, String cognome, Integer idSaggio, Boolean deleted);

    @Query("SELECT b FROM Biglietto b WHERE b.idUtente.nome LIKE %:nome% AND b.idUtente.cognome LIKE %:cognome% AND (b.deleted = :deleted OR b.deleted = true)")
    List<Biglietto> findBigliettoNameSurnameDeleted(String nome, String cognome, Boolean deleted);

    @Query("SELECT b from Biglietto b WHERE b.idUtente.id = :socioId AND b.deleted = false")
    List<Biglietto> findBigliettiSocio(Integer socioId);
}