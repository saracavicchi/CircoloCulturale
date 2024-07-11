package it.unife.cavicchidome.CircoloCulturale.services;

import it.unife.cavicchidome.CircoloCulturale.models.PrenotazioneSala;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.repositories.PrenotazioneSalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneSalaService {

    private final PrenotazioneSalaRepository prenotazioneSalaRepository;
    private final SocioService socioService;

    public PrenotazioneSalaService(PrenotazioneSalaRepository prenotazioneSalaRepository, SocioService socioService) {
        this.prenotazioneSalaRepository = prenotazioneSalaRepository;
        this.socioService = socioService;
    }

    @Transactional
    public List<PrenotazioneSala> getPrenotazioneBySocio(Integer idSocio) {
        return prenotazioneSalaRepository.findBySocio(idSocio);
    }

    @Transactional
    public Optional<PrenotazioneSala> getPrenotazioneById(Integer idSocio, Integer idPrenotazione) {
        Optional<PrenotazioneSala> prenotazione = prenotazioneSalaRepository.findById(idPrenotazione);
        Optional<Socio> socio = socioService.findSocioById(idSocio);
        if (prenotazione.isPresent() && socio.isPresent()) {
            if (prenotazione.get().getIdSocio().getId().equals(socio.get().getId()) || socio.get().getSegretario() != null) {
                return prenotazione;
            }
        }
        return Optional.empty();
    }
}
