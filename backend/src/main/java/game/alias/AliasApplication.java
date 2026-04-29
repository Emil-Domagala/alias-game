package game.alias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AliasApplication {

	public static void main(String[] args) {
		SpringApplication.run(AliasApplication.class, args);
	}

}
