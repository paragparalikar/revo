package com.revo.rms.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class PartDTO implements Serializable{
	private static final long serialVersionUID = 5206117100543113564L;

	private Long id;
	
	private Long kitId;

	@NotBlank
	private String name;
	
}
