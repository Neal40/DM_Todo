package DM_Todo.Todo;

import DM_Todo.Todo.todo.Todo;
import DM_Todo.Todo.todo.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}

	@Autowired
	private TodoRepository todoRepository;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Autowired
	public void authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
		builder.inMemoryAuthentication()
				.withUser("utilisateur")
				.password("motdepasse")
				.roles("UTILISATEUR");
	}

	@Bean
	public CommandLineRunner setUpBDD(){
		return (args) -> {

			List<Todo> todos = new ArrayList<>(){{
				add(new Todo(1L, "Faire les courses", "2023-11-15", "Pas fait"));
				add(new Todo(2L, "Réviser pour l'examen", "2023-11-17", "Pas fait"));
				add(new Todo(3L, "Rendre le devoir", "2023-11-20", "Fait"));

			}};
			todoRepository.saveAll(todos);
		};
	}

}
