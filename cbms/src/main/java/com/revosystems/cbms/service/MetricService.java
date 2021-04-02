package com.revosystems.cbms.service;

import org.springframework.stereotype.Service;

import com.revosystems.cbms.repository.MetricRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class MetricService {

	@NonNull
	@Delegate
	private final MetricRepository metricRepository;

}
