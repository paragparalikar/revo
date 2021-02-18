package com.revosystems.cbms.domain.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metric {
	
	@NotNull
	private Long thingId;

	@NotNull
	private Long sensorId;
	
	@NotNull
	private Double value;
	
	@NotNull
	private Long timestamp;
	
}
