package com.revosystems.llms.reason;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reasons")
public class ReasonController {

	@Autowired
	private ReasonService reasonService;
	
	@GetMapping
	public Iterable<Reason> findAll(){
		return reasonService.findAll();
	}
	
	@PutMapping
	@PostMapping
	public Reason save(@RequestBody @Valid Reason reason) {
		return reasonService.save(reason);
	}
	
	@DeleteMapping
	public void delete(Long id) {
		reasonService.deleteById(id);
	}
}
