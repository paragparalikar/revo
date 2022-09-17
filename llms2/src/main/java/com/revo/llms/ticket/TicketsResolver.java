package com.revo.llms.ticket;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.revo.llms.department.Department;
import com.revo.llms.department.DepartmentService;
import com.revo.llms.station.StationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketsResolver implements Function<byte[], Set<Ticket>> {

	private final StationService stationService;
	private final DepartmentService departmentService;
	
	@Override
	public Set<Ticket> apply(byte[] data) {
		final int deviceIndex = data[0];
		final int stationIdOffset = (deviceIndex - 1) * 15;
		final Set<Ticket> tikets = new HashSet<>();
		final List<Department> departments = departmentService.findAll();
		for(int index = 4; index < 33; index+=2) {
			final int stationId = index/2 - 1 + stationIdOffset;
			for(Department department : departments) {
				tikets.add(resolve(stationId, data[index], department));
			}
		}
		return tikets;
	}
	
	private Ticket resolve(int stationId, byte status, Department department) {
		final TicketStatus ticketStatus = resolve(status, department);
		final Ticket ticket = Ticket.builder()
				.station(stationService.findById(stationId).get())
				.status(ticketStatus)
				.department(department)
				.build();
		if(TicketStatus.OPEN.equals(ticketStatus)) ticket.setOpenTimestamp(new Date());
		if(TicketStatus.CLOSED.equals(ticketStatus)) ticket.setClosedTimestamp(new Date());
		return ticket;
	}
	
	private TicketStatus resolve(byte status, Department department) {
		return 0 < (status & ((byte)Math.pow(2, department.getCode()))) ?
				TicketStatus.OPEN : 
				TicketStatus.CLOSED;
	}


}
