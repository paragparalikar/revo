package com.revosystems.cbms.repository;

import java.util.List;

import com.revosystems.cbms.domain.model.Metric;

public interface MetricRepository {
	
	public Metric save(Metric metric);

	public List<Metric> query(Long thingId, Long sensorId, Long from, Long to);
	
}
