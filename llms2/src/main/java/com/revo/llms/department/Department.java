package com.revo.llms.department;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.revo.llms.common.HasName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@NoArgsConstructor
public class Department implements HasName {

	@Id
	@NonNull
	@NotNull
	private Integer id;
	
	@NotBlank
	@NonNull
	@Column(nullable = false, unique = true)
	private String name;
	
}
