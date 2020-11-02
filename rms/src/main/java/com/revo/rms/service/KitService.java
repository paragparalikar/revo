package com.revo.rms.service;

import org.springframework.stereotype.Service;

import com.revo.rms.model.Kit;
import com.revo.rms.repository.KitRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KitService {

	private final KitRepository kitRepository;
	
	public Kit getOne(@NonNull final Long id) {
		return kitRepository.getOne(id);
	}
	
}
