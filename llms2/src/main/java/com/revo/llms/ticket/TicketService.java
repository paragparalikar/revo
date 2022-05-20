package com.revo.llms.ticket;


import java.util.Date;
import java.util.List;
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

import com.revo.llms.port.PortPoller;
import com.revo.llms.port.PortResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TicketService {
	
	@Value("${llms.port-name:USB}")
	private String portName;
	
	@Value("${llms.port.request-timeout:5000}")
	private Long requestTimeoutMillis;
	
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
				.responseSize(35)
				.portName(portName)
				.callback(this::accept)
				.resolver(ticketsResolver)
				.portResolver(portResolver)
				.build();
	}
	
	@Scheduled(initialDelayString = "${poll.initial-delay.millis:1000}", 
			fixedDelayString = "${poll.fixed-delay.millis:5000}")
	public void poll() {
		portPoller.poll(new byte[]{1,3,0,0,0,15,5,(byte)206});
		portPoller.poll(new byte[]{2,3,0,0,0,15,5,(byte)253});
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
		if(null == persistentTicket) {
			if(TicketStatus.OPEN.equals(ticket.getStatus())) {
				log.trace("No previous ticket found, new open ticket received - {}", ticket);
				return ticket;
			} else {
				log.trace("No previous ticket found, received closed ticket, will be discarded - {}", ticket);
				return null;
			}
		} else if(ticket.isOpen() && persistentTicket.isClosed()) {
			log.info("Last ticket was closed, new open ticket received - {}", ticket);
			return ticket;
		} else if(persistentTicket.isOpen() && ticket.isClosed()) {
			log.info("Current ticket is open, it is being closed");
			persistentTicket.setStatus(ticket.getStatus());
			persistentTicket.setClosedTimestamp(ticket.getClosedTimestamp());
			return persistentTicket;
		}
		else return null;
	}
	
	public Page<Ticket> findByDepartmentIdIn(Set<Long> departmentIds, Pageable pageable){
		return ticketRepository.findByDepartmentIdIn(departmentIds, pageable);
	}

	public Optional<Ticket> findById(Long id){
		return ticketRepository.findById(id);
	}
	
	public long countByStatus(TicketStatus status) {
		return ticketRepository.countByStatus(status);
	}
	
	public List<Ticket> findByStatus(TicketStatus status){
		return ticketRepository.findByStatus(status);
	}
	
	public List<Ticket> findByOpenTimestampAfter(Date date){
		return ticketRepository.findByOpenTimestampAfter(date);
	}
	
	@Transactional
	public Ticket save(Ticket ticket) {
		return ticketRepository.save(ticket);
	}
}
