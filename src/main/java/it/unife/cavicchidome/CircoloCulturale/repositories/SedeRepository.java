package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.Sede;
import it.unife.cavicchidome.CircoloCulturale.models.Weekday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface SedeRepository extends JpaRepository<Sede, Integer> {
    @Query("SELECT s FROM Sede s JOIN s.sale sala WHERE sala.id = :idSala")
    Optional<Sede> findSedeByIdSala(@Param("idSala") Integer idSala);

    /*@Query("SELECT s FROM Sede s WHERE :date NOT IN s.giornoChiusura " +
            "AND :startTime BETWEEN (SELECT a.orarioApertura FROM s.orarioSede a WHERE a.id.giornoSettimana = :dow) " +
            "AND (SELECT a.orarioChiusura FROM s.orarioSede a WHERE a.id.giornoSettimana = :dow)" +
            "AND :endTime BETWEEN (SELECT a.orarioApertura FROM s.orarioSede a WHERE a.id.giornoSettimana = :dow) " +
            "AND (SELECT a.orarioChiusura FROM s.orarioSede a WHERE a.id.giornoSettimana = :dow)")
    Optional<Sede> findAvailableSede(LocalDate date, LocalTime startTime, LocalTime endTime, Weekday dow);*/

    @Query("SELECT s FROM Sede s WHERE s.id = :idSede AND NOT EXISTS (SELECT d FROM s.giornoChiusura d WHERE d = :date)")
    Optional<Sede> findAvailableSedeDate(Integer idSede, LocalDate date);
}