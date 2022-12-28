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
	
	List<Ticket> findByClosedTimestampAfterAndOpenTimestampBefore(Date from, Date to);
	
	long countByStatus(TicketStatus status);
	
	Page<Ticket> findByDepartmentIdIn(Set<Long> departmentIds, Pageable pageable);
	
	Page<Ticket> findByDepartmentIdInAndOpenTimestampBetween(Set<Long> departmentIds, Date from, Date to, Pageable pageable);
	
	Page<Ticket> findByDepartmentIdInAndOpenTimestampBetweenAndStatus(Set<Long> departmentIds, Date from, Date to, TicketStatus status, Pageable pageable);
	
	List<Ticket> findByDepartmentIdInAndOpenTimestampBetween(Set<Long> departmentIds, Date from, Date to);
	
	List<Ticket> findByDepartmentIdInAndOpenTimestampBetweenAndStatus(Set<Long> departmentIds, Date from, Date to, TicketStatus status);
	
	Optional<Ticket> findTopByStationIdAndDepartmentOrderByOpenTimestampDesc(Integer stationId, Department department);
	
}
