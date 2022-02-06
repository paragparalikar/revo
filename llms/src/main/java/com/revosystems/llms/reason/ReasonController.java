package com.revosystems.llms.reason;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revosystems.llms.Department;
import com.revosystems.llms.SecurityUtils;

@RestController
@RequestMapping("/reasons")
public class ReasonController {

	@Autowired private ReasonService reasonService;
	
	@GetMapping
	public Iterable<Reason> findAll(@AuthenticationPrincipal UserDetails userDetails){
		final Set<Department> departments = SecurityUtils.getAccessibleDepartments(userDetails);
		return reasonService.findByDepartmentIn(departments);
	}
	
	@GetMapping("/{id}")
	public Reason findById(@PathVariable Long id) {
		return reasonService.findById(id).orElse(null);
	}
	
	@GetMapping("/verify")
	public Boolean existsByTextAndDepartment(@RequestParam String text, @RequestParam Department department) {
		return reasonService.existsByTextAndDepartment(text, department);
	}
	
	@PostMapping
	public Reason create(@RequestBody @Valid Reason reason) {
		return reasonService.save(reason);
	}

	@PutMapping
	public Reason update(@RequestBody @Valid Reason reason) {
		return reasonService.save(reason);
	}
	
	@DeleteMapping("/{id}")
	public void delete(Long id) {
		reasonService.deleteById(id);
	}
}
