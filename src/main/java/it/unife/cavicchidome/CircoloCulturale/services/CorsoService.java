package it.unife.cavicchidome.CircoloCulturale.services;
import it.unife.cavicchidome.CircoloCulturale.repositories.CorsoRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.models.Corso;
import java.util.List;
import java.util.Optional;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CorsoService {
    private final CorsoRepository corsoRepository;
    private final SocioService socioService;


    public CorsoService(CorsoRepository corsoRepository, SocioService socioService){
        this.corsoRepository = corsoRepository;
        this.socioService = socioService;
    }

    @Transactional
    public boolean aggiungiCorsiBaseRuolo(HttpServletRequest request, HttpServletResponse response, Model model) {
        Optional<Socio> socioOpt = socioService.setSocioFromCookie(request, response, model);
        if (!socioOpt.isPresent()) {
            // Gestire il caso in cui il socio non è trovato
            return false;
        }
        Socio socio = socioOpt.get();
        List<Corso> corsi;
        if (socio.getDocente() != null) {
            // Il socio è un docente, quindi recupera i corsi insegnati da lui
            corsi = corsoRepository.findCorsiByDocenteId(socio.getDocente().getId()); //solo attivi
        } else if (socio.getSegretario() != null) {
            // Il socio è un segretario, quindi recupera tutti i corsi
            corsi = corsoRepository.findAllIfActiveTrue();
            System.out.println("Segretario");
        } else {
            // Gestire il caso in cui il socio non è né un docente né un segretario
            System.out.println("Errore identificativo socio");
            return false;
        }

        model.addAttribute("corsi", corsi);

        return true;
    }

    @Transactional
    List<Corso> findAllIfActiveTrue(){
        return corsoRepository.findAllIfActiveTrue();
    }

    @Transactional
    List<Corso> findCorsiByDocenteId(Integer docenteId){
        return corsoRepository.findCorsiByDocenteId(docenteId);
    }


}
