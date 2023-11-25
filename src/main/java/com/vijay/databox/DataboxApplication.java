package com.vijay.databox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.vijay.databox.persistence")
public class DataboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataboxApplication.class, args);
	}

}
