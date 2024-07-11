package it.unife.cavicchidome.CircoloCulturale.repositories;

import it.unife.cavicchidome.CircoloCulturale.models.CalendarioCorso;
import it.unife.cavicchidome.CircoloCulturale.models.CalendarioCorsoId;
import it.unife.cavicchidome.CircoloCulturale.models.Sala;
import it.unife.cavicchidome.CircoloCulturale.models.Weekday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.plaf.OptionPaneUI;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CalendarioCorsoRepository extends JpaRepository<CalendarioCorso, CalendarioCorsoId> {
    @Query("SELECT cc FROM CalendarioCorso cc JOIN cc.idCorso c WHERE c.idSala.id= :idSala AND cc.active = true AND c.active = true AND c.idSala.active= true AND cc.id.giornoSettimana = :giorno AND cc.active = true AND (" +
            "(cc.orarioInizio < :fine AND cc.orarioFine > :inizio) OR " + // Sovrappone l'inizio o la fine
            "(cc.orarioInizio >= :inizio AND cc.orarioFine <= :fine))") // Inizia e finisce all'interno
    List<CalendarioCorso> findCorsiSovrapposti(Weekday giorno, LocalTime inizio, LocalTime fine, Integer idSala); //controlla sala

    @Query("SELECT cc FROM CalendarioCorso cc WHERE cc.idCorso.id = :corsoId AND cc.active = true")
    List<CalendarioCorso> findByCorsoId( Integer corsoId);

    @Query("SELECT cc FROM CalendarioCorso cc WHERE cc.idCorso.id = :corsoId AND cc.id.giornoSettimana = :giorno AND cc.active = true")
    Optional<CalendarioCorso> findByCorsoAndGiornoSettimanaId(Integer corsoId, Weekday giorno);

    @Query("SELECT cc FROM CalendarioCorso cc JOIN cc.idCorso c WHERE cc.active = true AND c.active=true AND cc.id.giornoSettimana = :giorno AND cc.active = true AND (" +
            "(cc.orarioInizio < :fine AND cc.orarioFine > :inizio) OR " + // Sovrappone l'inizio o la fine
            "(cc.orarioInizio >= :inizio AND cc.orarioFine <= :fine))") // Inizia e finisce all'interno
    List<CalendarioCorso> findCorsiContemporanei(Weekday giorno, LocalTime inizio, LocalTime fine);

    @Query("SELECT cc FROM CalendarioCorso cc JOIN cc.idCorso c WHERE cc.id.giornoSettimana = :giorno AND cc.idCorso.id = :idCorso AND cc.active = true AND cc.idCorso.active =true AND (" +
            "(cc.orarioInizio < :fine AND cc.orarioFine > :inizio) OR " + // Sovrappone l'inizio o la fine
            "(cc.orarioInizio >= :inizio AND cc.orarioFine <= :fine))") // Inizia e finisce all'interno
    Optional<CalendarioCorso> findSeCorsoContemporaneo(Weekday giorno, Integer idCorso, LocalTime inizio, LocalTime fine); //solo calendari e corsi active

}