package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Integer> {
    Optional<Utente> findByCf(String cf);

    @Query("SELECT u FROM Utente u WHERE u.cf = :cf AND u.deleted = false")
    Optional<Utente> findByCfNotDeleted(String cf);


}