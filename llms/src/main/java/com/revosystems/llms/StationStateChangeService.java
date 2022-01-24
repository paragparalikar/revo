package com.revosystems.llms;

import java.util.Objects;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StationStateChangeService {

	private final StationStateChangeRepository repository;

	@Transactional
	public void saveAll(Set<StationStateChange> changes) {
		changes.forEach(this::save);
	}

	@Transactional
	public void save(StationStateChange change) {
		final StationStateChange lastChange = repository
				.findTopByStationIdAndTypeOrderByTimestampDesc(change.getStationId(), change.getType())
				.orElse(null);
		if(null == lastChange || !Objects.equals(lastChange.getState(), change.getState())) {
			final StationStateChange managed = repository.save(change);
			log.info("Persisted {}", managed);
		}
	}
	
}
