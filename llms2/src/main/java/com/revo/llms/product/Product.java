package com.revo.llms.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
public class Product {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank
	@Column(nullable = false, unique = true)
	private String name;
	
}

