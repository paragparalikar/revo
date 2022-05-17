package com.revo.llms.part;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import com.revo.llms.product.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "product_id"}))
public class Part {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank
	@Column(nullable = false)
	private String name;
	
	@ManyToOne(optional = false)
	private Product product;
}
