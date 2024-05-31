package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UtenteRepository extends JpaRepository<Utente, Integer>, JpaSpecificationExecutor<Utente> {
}