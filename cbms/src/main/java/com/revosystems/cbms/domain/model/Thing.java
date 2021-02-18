package com.revosystems.cbms.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.revosystems.cbms.domain.intf.Identifiable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Thing implements Identifiable<Long> {

	private Long id;
	
	@NotBlank
	@Size(max = 255)
	private String name;
	
}
