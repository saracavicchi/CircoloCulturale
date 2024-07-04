package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Tessera;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TesseraRepository extends JpaRepository<Tessera, String> {
}