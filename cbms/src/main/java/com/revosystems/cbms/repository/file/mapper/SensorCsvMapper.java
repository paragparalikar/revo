package com.revosystems.cbms.repository.file.mapper;

import org.apache.logging.log4j.util.Strings;

import com.revosystems.cbms.domain.model.Sensor;

public class SensorCsvMapper implements StringMapper<Sensor> {

	@Override
	public Sensor map(String text) {
		if(Strings.isBlank(text)) return null;
		final String[] tokens = text.split(",");
		return new Sensor(Long.parseLong(tokens[0]), tokens[1], tokens[2], 
				Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]));
	}

	@Override
	public String map(Sensor entity) {
		return null == entity ? null : String.join(",", String.valueOf(entity.getId()),
				entity.getName(), entity.getUnit(), String.valueOf(entity.getMin()), String.valueOf(entity.getMax()));
	}

}
