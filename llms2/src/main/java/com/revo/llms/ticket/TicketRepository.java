package com.revo.llms.ticket;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revo.llms.department.Department;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
	
	Page<Ticket> findByDepartmentIdIn(Set<Long> departmentIds, Pageable pageable);
	
	Page<Ticket> findByDepartmentIn(Set<Department> department, Pageable pageable);

	Optional<Ticket> findTopByStationIdAndDepartmentOrderByOpenTimestampDesc(Integer stationId, Department department);
	
}
