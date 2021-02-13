package com.revosystems.cbms.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank
	@Size(max = 255)
	@Column(unique = true, nullable = false)
	private String name;

	@NotBlank
	@Size(max = 255)
	@Column(unique = true, nullable = false)
	private String unit;
	
	@NotNull
	@Column(nullable = false)
	private Double min;
	
	@NotNull
	@Column(nullable = false)
	private Double max;
	
}
