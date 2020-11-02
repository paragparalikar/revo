package com.revo.rms.service;

import org.springframework.stereotype.Service;

import com.revo.rms.model.Station;
import com.revo.rms.repository.StationRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StationService {

	private final StationRepository stationRepository;
	
	public Station getOne(@NonNull final Long id) {
		return stationRepository.getOne(id);
	}
	
}
