package com.revosystems.llms.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class UserConfiguration {

	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private UserDetailsManager userDetailsManager;
	
	public CommandLineRunner createDefaultUser() {
		return args -> {
			if(!userDetailsManager.userExists("admin")) {
				log.warn("Default admin user not found, one will be created");
				final UserDetails userDetails = User.builder()
				.username("admin")
				.password("admin")
				.passwordEncoder(passwordEncoder::encode)
				.roles("ADMIN")
				.build();
				userDetailsManager.createUser(userDetails);
				log.info("Default admin user created");
			}
		};
	}
	
}
