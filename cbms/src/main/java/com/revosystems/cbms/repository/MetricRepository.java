package com.revosystems.cbms.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.revosystems.cbms.domain.model.Metric;

@Repository
public interface MetricRepository extends CrudRepository<Metric, Long> {

	public List<Metric> findBySensorIdAndTimestampAfterAndTimestampBefore(Long sensorId, Long from, Long to);
	
}
