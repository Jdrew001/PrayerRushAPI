package com.dtatkison.prayerrush.rushapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.dtatkison.prayerrush.rushapi.repository")
@SpringBootApplication
public class RushapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RushapiApplication.class, args);
	}
}
