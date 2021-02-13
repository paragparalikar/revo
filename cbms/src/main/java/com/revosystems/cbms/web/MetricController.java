package com.revosystems.cbms.web;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.revosystems.cbms.domain.model.Metric;
import com.revosystems.cbms.service.MetricService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/metrics")
public class MetricController {
	
	@NonNull
	private final MetricService metricService;
	
	@PostMapping
	public void create(@RequestBody @Valid final Metric metric) {
		metricService.save(metric);
	}
	
	@GetMapping
	@ResponseBody
	public List<Metric> query(@RequestParam final Long sensorId, 
			@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd hh-mm-ss") final Date from, 
			@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd hh-mm-ss") final Date to){
		return metricService.findBySensorIdAndTimestampAfterAndTimestampBefore(sensorId, from.getTime(), to.getTime());
	}

}
