package com.revo.llms.part;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.revo.llms.common.HasName;
import com.revo.llms.product.Product;

import lombok.Data;

@Data
@Entity
public class Part implements HasName {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank
	@Column(nullable = false, unique = true)
	private String name;
	
	@ManyToOne(optional = false)
	private Product product;
}
