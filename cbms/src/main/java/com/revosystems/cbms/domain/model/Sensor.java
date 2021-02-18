package com.revosystems.cbms.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.revosystems.cbms.domain.intf.Identifiable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sensor implements Identifiable<Long> {

	private Long id;
	
	@NotBlank
	@Size(max = 255)
	private String name;

	@NotBlank
	@Size(max = 255)
	private String unit;
	
	@NotNull
	private Double min;
	
	@NotNull
	private Double max;
	
}
