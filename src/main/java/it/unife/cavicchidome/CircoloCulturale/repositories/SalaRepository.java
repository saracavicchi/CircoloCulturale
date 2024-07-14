package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Sala;
import it.unife.cavicchidome.CircoloCulturale.models.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface SalaRepository extends JpaRepository<Sala, Integer> {
    Optional<Sala>  findById(Integer idSala);
    List<Sala> findAll();

    @Query("SELECT s FROM Sala s WHERE s.prenotabile = true AND s.active = true ORDER BY s.numeroSala")
    List<Sala> findAllPrenotabili();

    @Query("SELECT DISTINCT s.idSede FROM Sala s WHERE s.prenotabile = true AND s.active = true")
    List<Sede> findDistinctSedi();

    @Query("SELECT s FROM Sala s WHERE s.numeroSala = :numeroSala AND s.idSede = :idSede AND s.active = true AND s.idSede.active = true")
    Optional<Sala> findByNumeroSalaAndIdSede(Integer numeroSala, Sede idSede);

    @Query("SELECT s FROM Sala s WHERE s.idSede.id = :idSede AND s.active = true AND s.idSede.active = true")
    List<Sala> findAllBySedeId(Integer idSede);

}