package com.revo.llms.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

	boolean existsByCode(Integer code);
	
	boolean existsByNameIgnoreCase(String name);
	
}
