package com.revosystems.llms.ticket;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.revosystems.llms.Department;

@Component
public class TicketsResolver implements Function<byte[], Set<Ticket>> {

	@Override
	public Set<Ticket> apply(byte[] data) {
		final Set<Ticket> tikets = new HashSet<>();
		for(int index = 4; index < 33; index+=2) {
			final int stationId = index/2 - 1;
			for(Department department : Department.values()) {
				tikets.add(resolve(stationId, data[index], department));
			}
		}
		return tikets;
	}
	
	private Ticket resolve(int stationId, byte status, Department department) {
		final TicketStatus ticketStatus = resolve(status, department);
		final Ticket ticket = Ticket.builder()
				.stationId(stationId)
				.status(ticketStatus)
				.department(department)
				.build();
		if(TicketStatus.OPEN.equals(ticketStatus)) ticket.setOpenTimestamp(new Date());
		if(TicketStatus.CLOSED.equals(ticketStatus)) ticket.setClosedTimestamp(new Date());
		return ticket;
	}
	
	private TicketStatus resolve(byte status, Department department) {
		return 0 < (status & ((byte)Math.pow(2, department.ordinal()))) ?
				TicketStatus.OPEN : 
				TicketStatus.CLOSED;
	}


}
