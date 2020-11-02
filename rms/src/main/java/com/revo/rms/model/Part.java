package com.revo.rms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Part implements Serializable {
	private static final long serialVersionUID = -6043008130931675241L;

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private Kit kit;
	
	@Column(nullable = false, unique = true)
	private String name;

}
