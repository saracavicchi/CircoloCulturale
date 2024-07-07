package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Corso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CorsoRepository extends JpaRepository<Corso, Integer> {

    Optional<Corso> findByCategoriaAndGenereAndLivello(String categoria, String genere, String livello);

    @Query("SELECT DISTINCT c.categoria FROM Corso c")
    List<String> findDistinctCategoria();

    @Query("SELECT DISTINCT c.genere FROM Corso c")
    List<String> findDistinctGenere();

    @Query("SELECT DISTINCT c.livello FROM Corso c")
    List<String> findDistinctLivello();

    Optional<Corso> findById(Integer idCorso);
}


