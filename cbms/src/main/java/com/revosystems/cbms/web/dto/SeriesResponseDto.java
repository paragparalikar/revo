package com.revosystems.cbms.web.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;

@Getter
public class SeriesResponseDto {

	private final List<Double> values = new ArrayList<>();
	private final List<Date> timestamps = new ArrayList<>();
	private final List<PlotDto> plots = new ArrayList<>();
	
}
