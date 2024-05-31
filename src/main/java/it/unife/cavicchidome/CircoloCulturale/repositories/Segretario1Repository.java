package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Segretario1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface Segretario1Repository extends JpaRepository<Segretario1, Integer>, JpaSpecificationExecutor<Segretario1> {
}