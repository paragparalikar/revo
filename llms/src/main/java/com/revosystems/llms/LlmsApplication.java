package com.revosystems.llms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LlmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LlmsApplication.class, args);
	}
	
}
