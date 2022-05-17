package com.revo.llms.department;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class DepartmentService {

	@Delegate
	private final DepartmentRepository departmentRepository;
	
}
