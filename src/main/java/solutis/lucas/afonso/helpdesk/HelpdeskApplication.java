package solutis.lucas.afonso.helpdesk;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import solutis.lucas.afonso.helpdesk.entities.User;
import solutis.lucas.afonso.helpdesk.entities.UserRole;
import solutis.lucas.afonso.helpdesk.repository.UserRepository;

@EnableRabbit
@SpringBootApplication
public class HelpdeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpdeskApplication.class, args);
	}

	@Bean
	CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder,
			@Value("${helpdesk.admin.email}") String adminEmail,
			@Value("${helpdesk.admin.password}") String adminPassword) {
		return args -> {
			if (userRepository.findByEmail(adminEmail).isEmpty()) {
				User admin = new User(null, "Administrador", adminEmail,
						passwordEncoder.encode(adminPassword), UserRole.ADMIN, true, null);
				userRepository.save(admin);
			}
		};
	}

}
