package com.revosystems.llms.reason;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.revosystems.llms.Department;

import lombok.Data;

@Data
@Entity
public class Reason {

	@Id
	private Long id;
	
	@NotBlank
	@Column(nullable = false, unique = true)
	private String text;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Department department;
	
}
