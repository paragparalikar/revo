package com.revosystems.llms.ticket;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.revosystems.llms.Department;

@Repository
@RestResource
public interface TicketRepository extends PagingAndSortingRepository<Ticket, Long> {

	Optional<Ticket> findTopByStationIdAndDepartmentOrderByOpenTimestampDesc(Integer stationId, Department department);
	
}
