package com.revosystems.cbms.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Metric {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	private Long thingId;

	@NotNull
	private Long sensorId;
	
	@NotNull
	private Double value;
	
	@NotNull
	private Long timestamp;
	
}
