package com.revo.llms.ticket;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revo.llms.department.Department;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
	
	List<Ticket> findByStatus(TicketStatus status);
	
	List<Ticket> findByOpenTimestampAfter(Date minOpen);
	
	long countByStatus(TicketStatus status);
	
	Page<Ticket> findByDepartmentIdIn(Set<Long> departmentIds, Pageable pageable);
	
	Optional<Ticket> findTopByStationIdAndDepartmentOrderByOpenTimestampDesc(Integer stationId, Department department);
	
}
