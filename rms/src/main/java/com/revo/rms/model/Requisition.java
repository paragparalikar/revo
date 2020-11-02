package com.revo.rms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Requisition implements Serializable {
	private static final long serialVersionUID = -483546356390627075L;

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(optional = false)
	private Station station;

	@ManyToOne(optional = false)
	private Part part;
	
	@Column(nullable = false)
	private Long quantity;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private RequisitionStatus status = RequisitionStatus.OPEN;
	
}
