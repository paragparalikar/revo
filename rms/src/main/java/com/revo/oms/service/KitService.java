package com.revo.oms.service;

import org.springframework.stereotype.Service;

import com.revo.oms.model.Kit;
import com.revo.oms.repository.KitRepository;

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
