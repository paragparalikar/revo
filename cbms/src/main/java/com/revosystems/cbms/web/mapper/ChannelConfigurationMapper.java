package com.revosystems.cbms.web.mapper;

import org.springframework.stereotype.Component;

import com.revosystems.cbms.domain.model.ChannelConfiguration;
import com.revosystems.cbms.service.SensorService;
import com.revosystems.cbms.service.ThingService;
import com.revosystems.cbms.web.dto.ChannelConfigurationDto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChannelConfigurationMapper {

	@NonNull private final ThingService thingService;
	@NonNull private final SensorService sensorService;
	
	public ChannelConfiguration map(@NonNull final ChannelConfigurationDto dto) {
		final ChannelConfiguration channelConfiguration = new ChannelConfiguration();
		channelConfiguration.setChannel(dto.getChannel());
		channelConfiguration.setThing(null == dto.getThingId() ? null : thingService.findById(dto.getThingId()).get());
		channelConfiguration.setSensor(null == dto.getSensorId() ? null : sensorService.findById(dto.getSensorId()).get());
		return channelConfiguration;
	}
	
	public ChannelConfigurationDto map(@NonNull final ChannelConfiguration entity) {
		final ChannelConfigurationDto dto = new ChannelConfigurationDto();
		dto.setChannel(entity.getChannel());
		dto.setThingId(null == entity.getThing() ? null : entity.getThing().getId());
		dto.setSensorId(null == entity.getSensor() ? null : entity.getSensor().getId());
		return dto;
	}
	
}
