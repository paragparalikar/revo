package com.revosystems.llms.reason;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NonNull;

@Data
@Entity
public class Reason {

	@Id
	private String code;
	
	@NonNull
	@Column(nullable = false)
	private String text;
	
}
