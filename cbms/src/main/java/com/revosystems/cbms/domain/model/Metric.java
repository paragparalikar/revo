package com.revosystems.cbms.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Metric {
	
	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private Long sensorId;
	
	@NotNull
	private Double value;
	
	@NotNull
	private Long timestamp;
	
}
