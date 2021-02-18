package com.revosystems.cbms.repository.file.mapper;

import org.apache.logging.log4j.util.Strings;

import com.revosystems.cbms.domain.model.Thing;

public class ThingCsvMapper implements StringMapper<Thing> {

	@Override
	public Thing map(String text) {
		if(Strings.isBlank(text)) return null;
		final String[] tokens = text.split(",");
		return new Thing(Long.parseLong(tokens[0]), tokens[1]);
	}
	
	@Override
	public String map(Thing entity) {
		return null == entity ? null : String.join(",", String.valueOf(entity.getId()), entity.getName());
	}
	
}
