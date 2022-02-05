package com.revosystems.llms.ticket;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revosystems.llms.port.PortPoller;
import com.revosystems.llms.port.PortResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TicketService {
	
	@Value("${llms.port-name:CH340}")
	private String portName;
	
	@Autowired
	private TicketsResolver ticketsResolver;
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private PortResolver portResolver;
	
	private PortPoller<Set<Ticket>> portPoller;
	
	@PostConstruct
	public void init() {
		this.portPoller = PortPoller.<Set<Ticket>>builder()
				.request(new byte[]{1,3,0,0,0,15,5,(byte)206})
				.responseSize(35)
				.portName(portName)
				.callback(this::accept)
				.resolver(ticketsResolver)
				.portResolver(portResolver)
				.build();
	}
	
	@Scheduled(initialDelayString = "${poll.initial-delay.millis:1000}", 
			fixedDelayString = "${poll.fixed-delay.millis:1000}")
	public void poll() {
		portPoller.poll();
	}
	
	private void accept(Set<Ticket> tickets) {
		tickets.stream()
			.map(this::apply)
			.filter(Objects::nonNull)
			.forEach(ticket -> {
				log.info("Persisted ticket {}", ticketRepository.save(ticket));
			});
	}
	
	private Ticket apply(Ticket ticket) {
		final Ticket persistentTicket = ticketRepository.findTopByStationIdAndDepartmentOrderByOpenTimestampDesc(
				ticket.getStationId(), ticket.getDepartment()).orElse(null);
		if(null == persistentTicket) return ticket;
		else if(ticket.isOpen() && persistentTicket.isClosed()) return ticket;
		else if(persistentTicket.isOpen() && ticket.isClosed()) {
			persistentTicket.setStatus(ticket.getStatus());
			persistentTicket.setClosedTimestamp(ticket.getClosedTimestamp());
			return persistentTicket;
		}
		else return null;
	}
	
	
	public Page<Ticket> findAll(Pageable pageable){
		return ticketRepository.findAll(pageable);
	}

	public Optional<Ticket> findById(Long id){
		return ticketRepository.findById(id);
	}
	
	@Transactional
	public Ticket save(Ticket ticket) {
		return ticketRepository.save(ticket);
	}
}
