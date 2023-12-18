package com.example.CorporateEmployee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CorporateEmployeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorporateEmployeeApplication.class, args);
	}

}
