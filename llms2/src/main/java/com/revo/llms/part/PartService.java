package com.revo.llms.part;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class PartService {

	@Delegate
	private final PartRepository partRepository;
	
}
