package com.revosystems.cbms.repository.file.mapper;

import org.apache.logging.log4j.util.Strings;

import com.revosystems.cbms.domain.enumeration.Channel;
import com.revosystems.cbms.domain.model.ChannelConfiguration;
import com.revosystems.cbms.domain.model.Sensor;
import com.revosystems.cbms.domain.model.Thing;
import com.revosystems.cbms.repository.SensorRepository;
import com.revosystems.cbms.repository.ThingRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChannelConfigurationCsvMapper implements StringMapper<ChannelConfiguration> {

	@NonNull private final ThingRepository thingRepository;
	@NonNull private final SensorRepository sensorRepository;
	
	@Override
	public ChannelConfiguration map(String text) {
		if(Strings.isBlank(text)) return null;
		final String[] tokens = text.split(",");
		final Channel channel = Channel.valueOf(tokens[0]);
		final Thing thing = 1 >= tokens.length || Strings.isBlank(tokens[1]) ? null : thingRepository.findById(Long.parseLong(tokens[1])).get();
		final Sensor sensor = 2 >= tokens.length || Strings.isBlank(tokens[2]) ? null : sensorRepository.findById(Long.parseLong(tokens[2])).get();
		return new ChannelConfiguration(channel, thing, sensor);
	}

	@Override
	public String map(ChannelConfiguration entity) {
		return null == entity ? null : String.join(",", 
				entity.getChannel().name(), 
				String.valueOf(null == entity.getThing() ? "" : entity.getThing().getId()),
				String.valueOf(null == entity.getSensor() ? "" : entity.getSensor().getId()));
	}

}
