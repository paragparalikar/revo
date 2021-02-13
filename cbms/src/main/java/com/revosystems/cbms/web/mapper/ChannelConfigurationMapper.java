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
		channelConfiguration.setThing(thingService.findById(dto.getThingId()).get());
		channelConfiguration.setSensor(sensorService.findById(dto.getSensorId()).get());
		return channelConfiguration;
	}
	
	public ChannelConfigurationDto map(@NonNull final ChannelConfiguration entity) {
		final ChannelConfigurationDto dto = new ChannelConfigurationDto();
		dto.setChannel(entity.getChannel());
		dto.setThingId(entity.getThing().getId());
		dto.setSensorId(entity.getSensor().getId());
		return dto;
	}
	
}
