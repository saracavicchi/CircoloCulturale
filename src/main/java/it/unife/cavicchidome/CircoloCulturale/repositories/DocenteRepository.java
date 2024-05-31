package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DocenteRepository extends JpaRepository<Docente, Integer>, JpaSpecificationExecutor<Docente> {
}