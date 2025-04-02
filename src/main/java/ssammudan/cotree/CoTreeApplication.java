package ssammudan.cotree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoTreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoTreeApplication.class, args);
	}

}
