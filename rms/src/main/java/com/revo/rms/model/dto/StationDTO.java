package com.revo.rms.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class StationDTO implements Serializable {
	private static final long serialVersionUID = -6260995384717392489L;

	private Long id;
	
	@NotBlank
	private String name;
	
}
