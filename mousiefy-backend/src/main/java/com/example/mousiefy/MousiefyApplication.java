package com.example.mousiefy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MousiefyApplication {

	public static void main(String[] args) {
//		SpringApplication.run(MousiefyApplication.class, args);

		SpringApplicationBuilder builder = new SpringApplicationBuilder(MousiefyApplication.class);

		builder.headless(false);

		ConfigurableApplicationContext context = builder.run(args);
	}

}
