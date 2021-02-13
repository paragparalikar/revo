package com.revosystems.cbms.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.revosystems.cbms.domain.model.Sensor;

@Repository
public interface SensorRepository extends CrudRepository<Sensor, Long> {

}
