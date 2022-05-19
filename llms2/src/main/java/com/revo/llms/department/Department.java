package com.revo.llms.department;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@NoArgsConstructor
public class Department {

	@Id
	@GeneratedValue
	private Long id;
	
	@NonNull
	@NotNull
	@Column(nullable = false, unique = true)
	private Integer code;
	
	@NotBlank
	@NonNull
	@Column(nullable = false, unique = true)
	private String name;
	
}
