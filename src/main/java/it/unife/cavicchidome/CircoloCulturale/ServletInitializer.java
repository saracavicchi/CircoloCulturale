package it.unife.cavicchidome.CircoloCulturale;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// Questa classe estende SpringBootServletInitializer per consentire l'avvio dell'applicazione come un servlet all'interno di un contenitore web
public class ServletInitializer extends SpringBootServletInitializer {

	// Metodo che configura l'applicazione SpringBootServletInitializer
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		// Restituisce un oggetto SpringApplicationBuilder configurato con la classe principale dell'applicazione CircoloCulturaleApplication
		return application.sources(CircoloCulturaleApplication.class);
	}

}
