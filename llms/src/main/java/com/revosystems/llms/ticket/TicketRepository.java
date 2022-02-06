package com.revosystems.llms.ticket;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.revosystems.llms.Department;

@Repository
public interface TicketRepository extends PagingAndSortingRepository<Ticket, Long> {
	
	Page<Ticket> findByDepartmentIn(Set<Department> department, Pageable pageable);

	Optional<Ticket> findTopByStationIdAndDepartmentOrderByOpenTimestampDesc(Integer stationId, Department department);
	
}
