package com.revosystems.cbms.web.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetricDto {

	private Double value;
	
	private Date timestamp;
	
}
