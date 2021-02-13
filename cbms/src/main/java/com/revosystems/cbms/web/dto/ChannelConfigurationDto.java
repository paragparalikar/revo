package com.revosystems.cbms.web.dto;

import javax.validation.constraints.NotNull;

import com.revosystems.cbms.domain.enumeration.Channel;

import lombok.Data;

@Data
public class ChannelConfigurationDto {
	
	@NotNull
	private Channel channel;
	
	@NotNull
	private Long thingId;
	
	@NotNull
	private Long sensorId;

}
