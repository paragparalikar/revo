package com.revo.llms.reason;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.revo.llms.common.HasName;

import lombok.Data;

@Data
@Entity
public class Reason implements HasName {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank
	@Column(nullable = false, unique = true)
	private String text;
	
	@Override
	public String getName() {
		return getText();
	}
	
	@Override
	public void setName(String name) {
		setText(name);
	}
}
