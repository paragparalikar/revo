package com.revosystems.cbms.web.mapper;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.revosystems.cbms.domain.model.Metric;
import com.revosystems.cbms.web.dto.PlotDto;
import com.revosystems.cbms.web.dto.SeriesResponseDto;

@Component
public class SeriesMapper {
	
	private final long gap = 3600_000;

	public SeriesResponseDto map(List<Metric> metrics){
		final SeriesResponseDto response = new SeriesResponseDto();

		long lastTimestamp = 0;
		PlotDto plot = null;
		
		for(int index = 0; index < metrics.size(); index++) {
			final Metric metric = metrics.get(index);
			if(null == plot || gap < Math.abs(metric.getTimestamp() - lastTimestamp)) {
				plot = new PlotDto();
				plot.setFrom(new Date(metric.getTimestamp()));
				response.getPlots().add(plot);
			}
			
			final Double value = metric.getValue();
			plot.setTo(new Date(metric.getTimestamp()));
			plot.setMin(Math.min(plot.getMin(), value));
			plot.setMax(Math.max(plot.getMax(), value));
			plot.setAverage((plot.getAverage() * plot.getCount() + value)/(plot.getCount() + 1));
			plot.setCount(plot.getCount() + 1);
			response.getValues().add(metric.getValue());
			response.getTimestamps().add(new Date(metric.getTimestamp()));
			
			lastTimestamp = metric.getTimestamp();
		}
		
		return response;
	}
	
}
