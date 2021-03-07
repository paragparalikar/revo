package com.revosystems.cbms.web.dto;

import java.util.Comparator;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.revosystems.cbms.domain.enumeration.Channel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelConfigurationDto implements Comparable<ChannelConfigurationDto>{
	
	@NotNull
	private Channel channel;
	private Long thingId;
	private Long sensorId;
	
	@Override
	public int compareTo(ChannelConfigurationDto other) {
		return Objects.compare(channel, other.channel, Comparator.naturalOrder());
	}

}
