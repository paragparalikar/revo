package com.revo.oms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Kit {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;

}
