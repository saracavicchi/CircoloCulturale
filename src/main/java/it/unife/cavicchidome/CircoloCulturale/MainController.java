package it.unife.cavicchidome.CircoloCulturale;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class MainController {

	@RequestMapping("/hello")
	public String helloWorld() {
		return "helloworld";
	}

	@RequestMapping("/greet")
	public String greeter(@RequestParam String name, Model model) {
		model.addAttribute("name", name);
		return "greet";
	}
}
