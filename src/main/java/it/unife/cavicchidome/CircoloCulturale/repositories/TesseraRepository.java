package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Tessera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TesseraRepository extends JpaRepository<Tessera, Integer>, JpaSpecificationExecutor<Tessera> {
}