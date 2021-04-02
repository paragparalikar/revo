package com.revosystems.cbms.web.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.revosystems.cbms.domain.model.Sensor;
import com.revosystems.cbms.service.SensorService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/sensors")
public class SensorController {

	@NonNull
	private final SensorService sensorService;
	
	@ResponseBody
	@GetMapping
	public List<Sensor> findAll(){
		return StreamSupport.stream(sensorService.findAll().spliterator(), false)
				.collect(Collectors.toList());
	}
	
	@ResponseBody
	@GetMapping("/{id}")
	public Sensor findById(@NonNull @Positive @PathVariable final Long id) {
		return sensorService.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find sensor"));
	}
	
	@ResponseBody
	@PostMapping
	public Sensor create(@RequestBody @Valid final Sensor sensor) {
		return sensorService.save(sensor);
	}
	
	@ResponseBody
	@PutMapping
	public Sensor update(@RequestBody @Valid final Sensor sensor) {
		return sensorService.save(sensor);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@NonNull @Positive @PathVariable final Long id) {
		sensorService.deleteById(id);
	}
	
}
