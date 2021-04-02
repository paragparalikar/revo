package com.revosystems.cbms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CbmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CbmsApplication.class, args);
	}
	
}
