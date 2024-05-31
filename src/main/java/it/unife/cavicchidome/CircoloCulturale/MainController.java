package it.unife.cavicchidome.CircoloCulturale;

import it.unife.cavicchidome.CircoloCulturale.models.Socio;
import it.unife.cavicchidome.CircoloCulturale.repositories.SocioRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class MainController {

	SocioRepository socioRepository;

	MainController(SocioRepository socioRepository) {
		this.socioRepository = socioRepository;
	}

	@RequestMapping("/hello")
	public String helloWorld() {
		return "helloworld";
	}

	@RequestMapping("/greet")
	public String greeter(@RequestParam String name, Model model) {
		model.addAttribute("name", name);
		return "greet";
	}

	@GetMapping("/login")
	public String getLoginPage() {
		return "login";
	}

	@PostMapping("login")
	public String authenticate(@RequestParam String cf, @RequestParam String password, Model model) {
		Socio socio;
		if ((socio = socioRepository.authenticate(password, cf)) != null) {
			model.addAttribute("name", socio.getUtente().getNome());
			model.addAttribute("surname", socio.getUtente().getCognome());
			return "welcome-page";
		} else return "login";
	}
}
