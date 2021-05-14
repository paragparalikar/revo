package com.revosystems.cbms.repository;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.revosystems.cbms.domain.model.Metric;

public interface MetricRepository {
	
	public Metric save(Metric metric);

	public List<Metric> query(Long thingId, Long sensorId, Long from, Long to);

	void exportAll(OutputStream outputStream) throws IOException;

	void purgeAll() throws IOException;
	
}
