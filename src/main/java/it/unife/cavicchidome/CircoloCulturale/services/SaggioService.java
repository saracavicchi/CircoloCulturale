package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.Biglietto;
import it.unife.cavicchidome.CircoloCulturale.models.Corso;
import it.unife.cavicchidome.CircoloCulturale.models.Saggio;
import it.unife.cavicchidome.CircoloCulturale.repositories.CorsoRepository;
import it.unife.cavicchidome.CircoloCulturale.repositories.SaggioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.*;

@Service
public class SaggioService {

    private final SaggioRepository saggioRepository;
    private final CorsoRepository corsoRepository;

    SaggioService(SaggioRepository saggioRepository, CorsoRepository corsoRepository) {
        this.corsoRepository = corsoRepository;
        this.saggioRepository = saggioRepository;
    }
    
    @Transactional
    public List<Saggio> getNextMonth() {
        LocalDate untilDate = LocalDate.now().plusDays(60);
        return saggioRepository.getNextSaggi(untilDate);
    }

    @Transactional
    public Optional<Saggio> findSaggioById(Integer saggioId) {
        return saggioRepository.findById(saggioId);
    }

    @Transactional
    public List<Saggio> findAllSaggi () {
        return saggioRepository.findAll();
    }

    public int getAvailableTickets(Saggio saggio) {
        int maxAvailable = saggio.getMaxPartecipanti();
        int confirmedTickets = 0;
        for (Biglietto b : saggio.getBiglietti()) {
            if (!b.getDeleted() && b.getStatoPagamento() == 'c') {
                confirmedTickets += b.getQuantita();
            }
        }
        return maxAvailable - confirmedTickets;
    }

    public boolean validaInformazioniSaggio(
            String nome,
            LocalDate data,
            int numeroPartecipanti,
            Optional<String> descrizione,
            Optional<LocalTime> orarioInizio,
            Optional<LocalTime> orarioFine,
            String stato,
            String provincia,
            String citta,
            String via,
            String numeroCivico,
            List<Integer> corsiIds
    ) {
        String nomeIndirizzoPattern = "^[A-Za-z\\s\\-]+$";
        String descrizionePattern = "^[A-Za-z\\s\\-()]+$";


        if (nome == null || nome.trim().isEmpty() || !nome.matches(nomeIndirizzoPattern) || nome.length() > 30) {
            return false;
        }

        if (descrizione.isPresent() && !descrizione.get().matches(descrizionePattern)) {
            return false;
        }

        if (!stato.matches(nomeIndirizzoPattern) || !provincia.matches(nomeIndirizzoPattern) || !citta.matches(nomeIndirizzoPattern) || !via.matches(nomeIndirizzoPattern)) {
            return false;
        }

        int totalLength = stato.length() + provincia.length() + citta.length() + via.length();
        if (totalLength > 80) {
            return false;
        }

        if (data == null) return false;
        if (numeroPartecipanti <= 0) return false;
        if (orarioInizio.isPresent() && orarioFine.isPresent() && orarioInizio.get().isAfter(orarioFine.get())) {
            return false;
        }
        if (numeroCivico == null || numeroCivico.trim().isEmpty()) return false;
        if (corsiIds == null || corsiIds.isEmpty()) return false;

        return true;
    }

    @Transactional
    public boolean newSaggio(
            String nome,
            LocalDate data,
            int numeroPartecipanti,
            Optional<String> descrizione,
            Optional<LocalTime> orarioInizio,
            Optional<LocalTime> orarioFine,
            String stato,
            String provincia,
            String citta,
            String via,
            String numeroCivico,
            List<Integer> corsiIds
    ) {

        if (!validaInformazioniSaggio(
                nome,
                data,
                numeroPartecipanti,
                descrizione,
                orarioInizio,
                orarioFine,
                stato,
                provincia,
                citta,
                via,
                numeroCivico,
                corsiIds
        )) {
            return false;
        }

        if (saggioRepository.getSaggioByData(data).isPresent()) {
            throw new RuntimeException("Data già presente");
        }

        Saggio saggio = new Saggio();
        saggio.setNome(nome);
        saggio.setData(data);
        saggio.setMaxPartecipanti(numeroPartecipanti);
        if(descrizione.isPresent()) {
            saggio.setDescrizione(descrizione.get());
        }
        if(orarioInizio.isPresent()) {
            saggio.setOrarioInizio(orarioInizio.get());
        }
        if(orarioFine.isPresent()) {
            saggio.setOrarioFine(orarioFine.get());
        }

        saggio.setIndirizzo(stato, provincia, citta, via, numeroCivico);

        Set<Corso> corsi = new HashSet<>();
        for (Integer corsoId : corsiIds) {
            Corso corso = corsoRepository.findById(corsoId).orElseThrow(() -> new RuntimeException("Corso not found for id: " + corsoId));
            corsi.add(corso);
        }
        saggio.setCorsi(corsi);
        saggioRepository.save(saggio);

        return true;
    }

}
