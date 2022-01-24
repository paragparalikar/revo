package com.revosystems.llms;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationStateChangeRepository extends CrudRepository<StationStateChange, Long> {

	Optional<StationStateChange> findTopByStationIdAndTypeOrderByTimestampDesc(Long stationId, BreakdownType type);

}
