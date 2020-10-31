package com.revo.oms.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Requisition {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private Kit kit;

	@ManyToOne
	private Part part;
	
	private Long quantity;
	
	@Enumerated(EnumType.STRING)
	private RequisitionStatus status = RequisitionStatus.OPEN;

	@Override
	public String toString() {
		return "Requisition [id=" + id + ", kit=" + kit + ", part=" + part + ", quantity=" + quantity + ", status="
				+ status + "]";
	}
	
}
