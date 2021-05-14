package com.revosystems.cbms.web.controller;

import java.io.IOException;
import java.time.Duration;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.revosystems.cbms.domain.model.Metric;
import com.revosystems.cbms.service.DataCollectionService;
import com.revosystems.cbms.service.MetricService;
import com.revosystems.cbms.web.dto.SeriesRequestDto;
import com.revosystems.cbms.web.dto.SeriesResponseDto;
import com.revosystems.cbms.web.mapper.SeriesMapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/metrics")
public class MetricController {
	
	@NonNull private final SeriesMapper seriesMapper;
	@NonNull private final MetricService metricService;
	@NonNull private final DataCollectionService dataCollectionService;
	
	@PostMapping
	@ResponseBody
	public SeriesResponseDto query(@RequestBody @Valid final SeriesRequestDto request){
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(request.getTo());
		calendar.add(Calendar.DATE, 1);
		final List<Metric> metrics = metricService.query(request.getThingId(), request.getSensorId(), 
				request.getFrom().getTime(), calendar.getTimeInMillis());
		return seriesMapper.map(metrics);
	}
	
	@PutMapping
	public long setRequestDelayMinutes(@RequestParam final long delay) {
		dataCollectionService.setDelayMillis(Duration.ofMinutes(delay).toMillis());
		return Duration.ofMillis(dataCollectionService.getDelayMillis()).toMinutes();
	}
	
	@GetMapping("/delay")
	public long getRequestDelayMinutes() {
		return Duration.ofMillis(dataCollectionService.getDelayMillis()).toMinutes();
	}
	
	@GetMapping
	public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
		metricService.exportAll(response.getOutputStream());
	}
	
	@DeleteMapping
	public void purge() throws IOException {
		metricService.purgeAll();
	}
	
}
