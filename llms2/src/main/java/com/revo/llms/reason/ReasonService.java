package com.revo.llms.reason;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class ReasonService {

	@Delegate
	private final ReasonRepository reasonRepository;
	
}
