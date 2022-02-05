package com.revosystems.llms.reason;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
public class Reason {

	@Id
	private Long id;
	
	@NotBlank
	@Column(nullable = false)
	private String text;
	
}
