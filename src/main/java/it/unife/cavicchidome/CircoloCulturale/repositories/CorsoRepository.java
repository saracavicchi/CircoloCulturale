package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Corso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CorsoRepository extends JpaRepository<Corso, Integer> {

    @Query("SELECT DISTINCT c.categoria FROM Corso c")
    List<String> findDistinctCategoria();

    @Query("SELECT DISTINCT c.genere FROM Corso c")
    List<String> findDistinctGenere();

    @Query("SELECT DISTINCT c.livello FROM Corso c")
    List<String> findDistinctLivello();

    @Query("SELECT c FROM Corso c WHERE c.categoria = :categoria AND c.genere = :genere AND c.livello = :livello")
    Optional<Corso> findByCategoriaAndGenereAndLivello(@Param("categoria") String categoria, @Param("genere") String genere, @Param("livello") String livello);

    @Query("SELECT c FROM Corso c WHERE c.id = :idCorso AND c.active = true")
    Optional<Corso> findById(Integer idCorso);

    @Query("SELECT c FROM Corso c WHERE c.id = :idCorso")
    Optional<Corso> findByIdAll(Integer idCorso);

    @Query("SELECT c FROM Corso c JOIN c.docenti d WHERE c.active=true AND c.d.id = :docenteId")
    List<Corso> findCorsiByDocenteId( Integer docenteId);
}


