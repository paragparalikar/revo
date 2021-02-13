package com.revosystems.cbms.service;

import org.springframework.stereotype.Service;

import com.revosystems.cbms.repository.SensorRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class SensorService {

	@NonNull
	@Delegate
	private final SensorRepository sensorRepository;
	
}
