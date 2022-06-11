package com.revo.llms.ticket.event;

import com.revo.llms.ticket.Ticket;

import lombok.NonNull;
import lombok.Value;

@Value
public class TicketClosedEvent {

	@NonNull private final Ticket ticket;
	
}
