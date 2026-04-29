package com.hadasim.hadasim_trip_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // <-- תוסיפי את השורה הזו
public class HadasimTripManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HadasimTripManagerApplication.class, args);
	}

}
