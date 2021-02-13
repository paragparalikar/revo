package com.revosystems.cbms.web.mapper;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.revosystems.cbms.domain.model.Metric;
import com.revosystems.cbms.web.dto.MetricDto;
import com.revosystems.cbms.web.dto.PlotDto;
import com.revosystems.cbms.web.dto.SeriesResponseDto;

@Component
public class SeriesMapper {
	
	private final long gap = 3600_000;

	public SeriesResponseDto map(List<Metric> metrics){
		final SeriesResponseDto response = new SeriesResponseDto();
		final List<PlotDto> plots = new LinkedList<>();
		response.setPlots(plots);

		long lastTimestamp = 0;
		PlotDto plot = null;
		
		for(Metric metric : metrics) {
			if(gap < Math.abs(metric.getTimestamp() - lastTimestamp)) {
				plot = new PlotDto();
				plots.add(plot);
			}
			
			final Double value = metric.getValue();
			plot.setMin(Math.min(plot.getMin(), value));
			plot.setMax(Math.max(plot.getMax(), value));
			plot.setAverage((plot.getAverage() * plot.getCount() + value)/(plot.getCount() + 1));
			plot.setCount(plot.getCount() + 1);
			plot.getItems().add(map(metric));
			
			lastTimestamp = metric.getTimestamp();
		}
		
		return response;
	}
	
	private MetricDto map(Metric metric) {
		return new MetricDto(metric.getValue(), new Date(metric.getTimestamp()));
	}
	
}
