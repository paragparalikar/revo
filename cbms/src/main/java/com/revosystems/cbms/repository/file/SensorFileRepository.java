package com.revosystems.cbms.repository.file;

import java.nio.file.Path;

import com.revosystems.cbms.domain.model.Sensor;
import com.revosystems.cbms.repository.SensorRepository;
import com.revosystems.cbms.repository.file.mapper.StringMapper;

import lombok.NonNull;

public class SensorFileRepository extends GeneratedIdFileRepository<Sensor> implements SensorRepository {

	public SensorFileRepository(@NonNull Path path, @NonNull StringMapper<Sensor> mapper) {
		super(path, mapper);
	}
}
