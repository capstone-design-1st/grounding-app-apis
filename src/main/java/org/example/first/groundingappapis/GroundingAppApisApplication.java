package org.example.first.groundingappapis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GroundingAppApisApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroundingAppApisApplication.class, args);
	}

}
