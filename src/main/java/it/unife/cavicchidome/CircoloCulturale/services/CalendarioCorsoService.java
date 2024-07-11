package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.CalendarioCorso;
import it.unife.cavicchidome.CircoloCulturale.models.Weekday;
import it.unife.cavicchidome.CircoloCulturale.repositories.CalendarioCorsoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarioCorsoService {
    private final CalendarioCorsoRepository calendarioCorsoRepository;

    public CalendarioCorsoService(CalendarioCorsoRepository calendarioCorsoRepository) {
        this.calendarioCorsoRepository = calendarioCorsoRepository;
    }

    @Transactional //solo calendari, corsi e sale active
    List<CalendarioCorso> findCorsiSovrapposti(Weekday giorno, LocalTime inizio, LocalTime fine, Integer idSala){
        return calendarioCorsoRepository.findCorsiSovrapposti(giorno, inizio, fine, idSala);
    }

    @Transactional //solo calendari active
    List<CalendarioCorso> findByCorsoId( Integer corsoId){
        return calendarioCorsoRepository.findByCorsoId(corsoId);
    }

    @Transactional //solo calendari active
    Optional<CalendarioCorso> findByCorsoAndGiornoSettimanaId(Integer corsoId, Weekday giorno){
        return calendarioCorsoRepository.findByCorsoAndGiornoSettimanaId(corsoId, giorno);
    }

    @Transactional //solo calendari active
    List<CalendarioCorso> findCorsiContemporanei(Weekday giorno, LocalTime inizio, LocalTime fine){
        return calendarioCorsoRepository.findCorsiContemporanei(giorno, inizio, fine);
    }

    @Transactional //solo calendari e corsi active
    Optional<CalendarioCorso> findSeCorsoContemporaneo(Weekday giorno, Integer idCorso, LocalTime inizio, LocalTime fine){
        return calendarioCorsoRepository.findSeCorsoContemporaneo(giorno, idCorso, inizio, fine);
    }
}
