package com.revo.oms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.revo.oms.model.Kit;
import com.revo.oms.model.Part;
import com.revo.oms.repository.KitRepository;
import com.revo.oms.repository.PartRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DevConfiguration {
	
	@Bean
	public CommandLineRunner createKits(final KitRepository kitRepository) {
		return args -> {
			for(int index = 0; index < 4; index++) {
				final Kit kit = new Kit();
				kit.setName("K" + index);
				kitRepository.saveAndFlush(kit);
				log.info("Created kit {}", kit);
			}
		};
	}
	
	@Bean
	public CommandLineRunner createParts(final PartRepository partRepository) {
		return args -> {
			for(int index = 0; index < 10; index++) {
				final Part part = new Part();
				part.setName("P" + index);
				partRepository.saveAndFlush(part);
				log.info("Created part {}", part);
			}
		};
	}
	
}
