package com.revosystems.cbms.service;

import org.springframework.stereotype.Service;

import com.revosystems.cbms.repository.ThingRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class ThingService {

	@NonNull
	@Delegate
	private final ThingRepository thingRepository;
	
}
