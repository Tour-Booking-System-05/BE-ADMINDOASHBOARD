package com.travel.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class TravelManagermentApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelManagermentApplication.class, args);
	}

}
