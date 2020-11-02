package com.revo.rms.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class KitDTO implements Serializable {
	private static final long serialVersionUID = -4263651240451159734L;

	private Long id;
	
	@NotBlank
	private String name;
	
}
