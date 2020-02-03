package intra.poleemploi;

import intra.poleemploi.dao.AppliRepository;
import intra.poleemploi.dao.ContentRepository;
import intra.poleemploi.dao.UserAppRepository;
import intra.poleemploi.entities.Appli;
import intra.poleemploi.entities.Content;
import intra.poleemploi.entities.RoleApp;
import intra.poleemploi.entities.UserApp;
import intra.poleemploi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class GuidanceBackApplication {
	@Autowired
	private RepositoryRestConfiguration repositoryRestConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(GuidanceBackApplication.class, args);
	}

	@Bean
	CommandLineRunner start(AppliRepository appliRepository, ContentRepository contentRepository, AuthService authService, UserAppRepository userAppRepository){
		return args -> {
			repositoryRestConfiguration.exposeIdsFor(Appli.class, Content.class, UserApp.class, RoleApp.class);
		};
	}
	// créer BCryptPasswordEncoder au démarrage de l'appli pour injection dans couche Service
	@Bean
	BCryptPasswordEncoder getBCPE() {
		return new BCryptPasswordEncoder();
	}
}

