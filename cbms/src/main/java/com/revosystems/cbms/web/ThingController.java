package com.revosystems.cbms.web;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
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

import com.revosystems.cbms.domain.model.Thing;
import com.revosystems.cbms.service.ThingService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/things")
@RequiredArgsConstructor
public class ThingController {

	@NonNull
	private final ThingService thingService;
	
	@GetMapping
	@ResponseBody
	public List<Thing> findAll(){
		return StreamSupport.stream(thingService.findAll().spliterator(), false)
				.collect(Collectors.toList());
	}

	@ResponseBody
	@GetMapping("/{id}")
	public Thing findById(@NonNull @Positive @PathVariable final Long id) {
		return thingService.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find thing"));
	}
	
	@ResponseBody
	@PostMapping
	public Thing create(@RequestBody @Valid final Thing thing) {
		return thingService.save(thing);
	}
	
	@ResponseBody
	@PutMapping
	public Thing update(@RequestBody @Valid final Thing thing) {
		return thingService.save(thing);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@NonNull @Positive @PathVariable final Long id) {
		thingService.deleteById(id);
	}
	
}
