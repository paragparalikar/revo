package com.revo.oms.service;

import org.springframework.stereotype.Service;

import com.revo.oms.model.Part;
import com.revo.oms.repository.PartRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartService {

	private final PartRepository partRepository;
	
	public Part getOne(@NonNull final Long id) {
		return partRepository.getOne(id);
	}
	
}
