package com.revosystems.cbms.web.dto;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class PlotDto {
	
	private Long count = 0l;
	private Double average = 0d;
	private Double min = Double.MAX_VALUE;
	private Double max = Double.MIN_VALUE;
	private final List<MetricDto> items = new LinkedList<>();

}
