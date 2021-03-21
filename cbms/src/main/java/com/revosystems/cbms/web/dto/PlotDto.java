package com.revosystems.cbms.web.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PlotDto {
	
	private Date to;
	private Date from;
	private Long count = 0l;
	private Double average = 0d;
	private Double min = Double.MAX_VALUE;
	private Double max = Double.MIN_VALUE;
	
}
