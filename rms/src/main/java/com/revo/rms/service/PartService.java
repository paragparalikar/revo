package com.revo.rms.service;

import org.springframework.stereotype.Service;

import com.revo.rms.model.Part;
import com.revo.rms.repository.PartRepository;

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
