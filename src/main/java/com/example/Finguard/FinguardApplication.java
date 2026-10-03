package com.example.Finguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.Finguard")
public class FinguardApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinguardApplication.class, args);
	}

}
