package com.revosystems.cbms.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Thing {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank
	@Size(max = 255)
	@Column(unique = true, nullable = false)
	private String name;
	
}
