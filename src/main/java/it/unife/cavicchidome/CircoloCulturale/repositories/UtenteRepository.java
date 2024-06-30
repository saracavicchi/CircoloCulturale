package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UtenteRepository extends JpaRepository<Utente, Integer> {
    Utente findByCf(String cf);

    @Query("SELECT COALESCE(MAX(u.id), 0) FROM Utente u")
    Integer findMaxId();
}