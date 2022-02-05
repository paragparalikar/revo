package com.revosystems.llms.reason;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReasonService {

	@Autowired
	private ReasonRepository reasonRepository;
	
	public Iterable<Reason> findAll(){
		return reasonRepository.findAll();
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
