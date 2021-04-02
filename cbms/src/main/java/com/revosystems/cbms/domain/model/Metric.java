package com.revosystems.cbms.domain.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metric {
	
	public static Metric build(
			@NonNull final ChannelConfiguration channelConfiguration,
			@NonNull final Long timestamp,
			@NonNull final Double value) {
		final Metric metric = new Metric();
		metric.setValue(value);
		metric.setTimestamp(timestamp);
		metric.setThingId(channelConfiguration.getThing().getId());
		metric.setSensorId(channelConfiguration.getSensor().getId());
		return metric;
	}
	
	@NotNull
	private Long thingId;

	@NotNull
	private Long sensorId;
	
	@NotNull
	private Double value;
	
	@NotNull
	private Long timestamp;
	
}
