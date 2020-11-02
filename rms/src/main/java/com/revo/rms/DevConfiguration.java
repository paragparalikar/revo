package com.revo.rms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import com.revo.rms.model.Kit;
import com.revo.rms.model.Part;
import com.revo.rms.model.Requisition;
import com.revo.rms.model.Station;
import com.revo.rms.repository.KitRepository;
import com.revo.rms.repository.PartRepository;
import com.revo.rms.repository.RequisitionRepository;
import com.revo.rms.repository.StationRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DevConfiguration {
	
	@Bean
	@Order(0)
	public CommandLineRunner createStations(final StationRepository stationRepository) {
		return args -> {
			for (int index = 0; index < 4; index++) {
				final Station station = new Station();
				station.setName("S"+index);
				stationRepository.saveAndFlush(station);
			}
		};
	}

	@Bean
	@Order(1)
	public CommandLineRunner createKits(final KitRepository kitRepository) {
		return args -> {
			for (int index = 0; index < 4; index++) {
				final Kit kit = new Kit();
				kit.setName("K" + index);
				kitRepository.saveAndFlush(kit);
			}
		};
	}

	@Bean
	@Order(2)
	public CommandLineRunner createParts(final KitRepository kitRepository, final PartRepository partRepository) {
		return args -> {
			for (final Kit kit : kitRepository.findAll()) {
				for (int index = 0; index < 10; index++) {
					final Part part = new Part();
					part.setKit(kit);
					part.setName(kit.getName() + "-P" + index);
					partRepository.saveAndFlush(part);
				}
			}

		};
	}
	
	

	//@Bean
	@Order(3)
	public CommandLineRunner createRequisitions(
			final PartRepository partRepository, 
			final StationRepository stationRepository,
			final RequisitionRepository requisitionRepository) {
		return args -> {
			for(final Station station : stationRepository.findAll()) {
				for (final Part part : partRepository.findAll()) {
					final Requisition requisition = new Requisition();
					requisition.setPart(part);
					requisition.setStation(station);
					requisition.setQuantity(10l);
					requisitionRepository.saveAndFlush(requisition);
				}
			}
		};
	}

}
