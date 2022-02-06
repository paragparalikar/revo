package com.revosystems.llms.reason;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.revosystems.llms.Department;

@Repository
public interface ReasonRepository extends PagingAndSortingRepository<Reason, Long> {
	
	Boolean existsByTextAndDepartment(String text, Department department);

	List<Reason> findAllByDepartmentIn(Iterable<Department> departments);
	
}
