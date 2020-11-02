package com.revo.rms.model.dto;

import java.io.Serializable;

import javax.validation.constraints.Positive;

import com.revo.rms.model.RequisitionStatus;

import lombok.Data;

@Data
public class RequisitionDTO implements Serializable {
	private static final long serialVersionUID = -5844452070098730640L;

	private Long id;
	
	private Long partId;
	
	private Long stationId;
	
	@Positive
	private Long quantity;
	
	private RequisitionStatus status;
	
}
