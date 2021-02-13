package com.revosystems.cbms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.revosystems.cbms.domain.model.Metric;

@Repository
public interface MetricRepository extends CrudRepository<Metric, Long> {

	@Query("from Metric where thingId = :thingId and sensorId = :sensorId and timestamp > :from and timestamp < :to order by timestamp asc")
	public List<Metric> query(
			@Param("thingId") Long thingId,
			@Param("sensorId") Long sensorId,
			@Param("from") Long from,
			@Param("to") Long to);
	
}
