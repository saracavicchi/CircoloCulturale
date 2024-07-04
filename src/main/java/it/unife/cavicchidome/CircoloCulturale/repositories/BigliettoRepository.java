package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Biglietto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BigliettoRepository extends JpaRepository<Biglietto, Integer> {
}