package com.revo.llms.station;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StationService {

	private final StationRepository stationRepository;
	
	@PostConstruct
	public void init() {
		if(0 == stationRepository.count()) {
			for(int id = 1; id <= 32; id++) {
				stationRepository.save(new Station(id, String.valueOf(id)));
			}
		}
	}
	
	public boolean existsByNameIgnoreCaseAndIdNot(String name, Integer id) {
		return stationRepository.existsByNameIgnoreCaseAndIdNot(name, id);
	}

	public Optional<Station> findById(Integer id){
		return stationRepository.findById(id);
	}
	
	public List<Station> findAll(){
		return stationRepository.findAllByOrderByIdAsc();
	}
	
	public Station save(Station station) {
		return stationRepository.saveAndFlush(station);
	}
	
	public void saveAll(Collection<Station> stations) {
		stationRepository.saveAllAndFlush(stations);
	}
	
}
