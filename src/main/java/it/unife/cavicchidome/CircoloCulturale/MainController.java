package it.unife.cavicchidome.CircoloCulturale;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

// Questa classe funge da controller principale per gestire le richieste HTTP
@Controller
public class MainController {

	// Repository per l'accesso ai dati degli utenti
	SocioRepository socioRepository;

	// Costruttore che inizializza il repository
	MainController(SocioRepository socioRepository) {
		this.socioRepository = socioRepository;
	}

	// Endpoint per una semplice pagina di saluto
	@RequestMapping("/hello")
	public String helloWorld() {
		return "helloworld";
	}

	// Endpoint per il saluto personalizzato
	@RequestMapping("/greet")
	public String greeter(@RequestParam String name, Model model) {
		model.addAttribute("name", name);
		return "greet";
	}

	// Endpoint per visualizzare la pagina di login
	@GetMapping("/login")
	public String getLoginPage() {
		return "login";
	}

	// Endpoint per autenticare un utente
	@PostMapping("login")
	public String authenticate(@RequestParam String cf, @RequestParam String password, Model model) {
		Socio socio;
		// Verifica l'autenticazione dell'utente
		if ((socio = socioRepository.authenticate(password, cf)) != null) {
			// Se l'autenticazione ha successo, visualizza una pagina di benvenuto
			model.addAttribute("name", socio.getUtente().getNome());
			model.addAttribute("surname", socio.getUtente().getCognome());
			return "welcome-page";
		} else
			// Se l'autenticazione fallisce, reindirizza all pagina di login
			return "login";
	}
}
