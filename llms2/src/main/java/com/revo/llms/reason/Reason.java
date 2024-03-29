package com.revo.llms.reason;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.revo.llms.category.Category;

import lombok.Data;

@Data
@Entity
public class Reason {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank
	@Column(nullable = false, unique = true)
	private String text;
	
	@ManyToOne(optional = false)
	private Category category;

}
