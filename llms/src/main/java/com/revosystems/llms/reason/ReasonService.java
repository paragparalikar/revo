package com.revosystems.llms.reason;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revosystems.llms.Department;

@Service
@Transactional
public class ReasonService {

	@Autowired
	private ReasonRepository reasonRepository;
	
	public Boolean existsByTextAndDepartment(String text, Department department) {
		return reasonRepository.existsByTextAndDepartment(text, department);
	}
	
	public List<Reason> findByDepartmentIn(Iterable<Department> departments){
		return reasonRepository.findAllByDepartmentIn(departments);
	}
	
	public Optional<Reason> findById(Long id) {
		return reasonRepository.findById(id);
	}
	
	@Transactional
	public Reason save(Reason reason) {
		return reasonRepository.save(reason);
	}
	
	@Transactional
	public void deleteById(Long id) {
		reasonRepository.deleteById(id);
	}
	
}
