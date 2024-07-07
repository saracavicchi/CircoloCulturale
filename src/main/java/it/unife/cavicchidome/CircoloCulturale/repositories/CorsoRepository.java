package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Corso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CorsoRepository extends JpaRepository<Corso, Integer> {

    Optional<Corso> findByCategoriaAndGenereAndLivello(String categoria, String genere, String livello);

    Optional<Corso> findById(Integer idCorso);
}


