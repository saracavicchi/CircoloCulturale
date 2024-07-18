package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.OrarioSede;
import it.unife.cavicchidome.CircoloCulturale.models.Sede;
import it.unife.cavicchidome.CircoloCulturale.models.Weekday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface SedeRepository extends JpaRepository<Sede, Integer> {
    @Query("SELECT s FROM Sede s JOIN s.sale sala WHERE sala.id = :idSala AND s.active = true AND sala.active = true")
    Optional<Sede> findSedeByIdSalaActive(@Param("idSala") Integer idSala);

    /*@Query("SELECT s FROM Sede s WHERE :date NOT IN s.giornoChiusura " +
            "AND :startTime BETWEEN (SELECT a.orarioApertura FROM s.orarioSede a WHERE a.id.giornoSettimana = :dow) " +
            "AND (SELECT a.orarioChiusura FROM s.orarioSede a WHERE a.id.giornoSettimana = :dow)" +
            "AND :endTime BETWEEN (SELECT a.orarioApertura FROM s.orarioSede a WHERE a.id.giornoSettimana = :dow) " +
            "AND (SELECT a.orarioChiusura FROM s.orarioSede a WHERE a.id.giornoSettimana = :dow)")
    Optional<Sede> findAvailableSede(LocalDate date, LocalTime startTime, LocalTime endTime, Weekday dow);*/

    @Query("SELECT s FROM Sede s WHERE s.id = :idSede AND s.active = true AND NOT EXISTS (SELECT d FROM s.giornoChiusura d WHERE d = :date) AND EXISTS (SELECT o FROM s.orarioSede o WHERE o.id.giornoSettimana = :dow)")
    Optional<Sede> findAvailableSedeDate(Integer idSede, LocalDate date, Weekday dow); //TODO: ho aggiunto ACTIVE, vedere se va bene

    @Query("SELECT o FROM Sede s JOIN s.orarioSede o WHERE s.id = :idSede AND o.id.giornoSettimana = :dow AND s.active = true")
    OrarioSede findOrarioSede(Integer idSede, Weekday dow); //TODO: ho aggiunto ACTIVE, vedere se va bene

    @Query("SELECT s FROM Sede s WHERE s.nome = :nome")
    Optional<Sede> findSedeByNomeAll(String nome);

    @Query("SELECT s FROM Sede s WHERE s.nome = :nome AND s.active = true")
    Optional<Sede> findSedeByNomeActive(String nome);

    @Query("SELECT s FROM Sede s WHERE s.indirizzo = :indirizzo")
    Optional<Sede> findSedeByIndirizzoAll(String indirizzo);

    @Query("SELECT s FROM Sede s WHERE s.indirizzo = :indirizzo AND s.active = true")
    Optional<Sede> findSedeByIndirizzoActive(String indirizzo);

    @Query("SELECT s FROM Sede s WHERE s.active = true")
    List<Sede> findAllActive();

    @Query("SELECT s FROM Sede s ")
    List<Sede> findAllEvenIfNotActive();

    @Query("SELECT s FROM Sede s WHERE s.active = true AND s.id = :id")
    Optional<Sede> findByIdActive(Integer id);

    @Query("SELECT s FROM Sede s WHERE s.id = :id")
    Optional<Sede> findByIdAll(Integer id);

    @Query("SELECT s FROM Sede s WHERE s.active = true ORDER BY s.id ASC LIMIT 1")
    Optional<Sede> findActiveSedeWithMinId();

    @Query("SELECT s FROM Sede s WHERE s.segretario.id = :idSegretario AND s.active = true AND s.segretario.active = true")
    Optional<Sede> findSedeByIdSegretario(Integer idSegretario);

}