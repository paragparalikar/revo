package com.revo.llms.ticket;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revo.llms.common.Broadcaster;
import com.revo.llms.port.PortPoller;
import com.revo.llms.port.PortResolver;
import com.revo.llms.ticket.event.TicketClosedEvent;
import com.revo.llms.ticket.event.TicketOpenedEvent;

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
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
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
			fixedDelayString = "${poll.fixed-delay.millis:1000}")
	public void poll() throws InterruptedException {
		portPoller.poll(new byte[]{1,3,0,0,0,15,5,(byte)206});
		Thread.sleep(500);
		portPoller.poll(new byte[]{2,3,0,0,0,15,5,(byte)253});
	}
	
	private void accept(Set<Ticket> tickets) {
		tickets.stream()
			.map(this::apply)
			.filter(Objects::nonNull)
			.forEach(ticket -> {
				final Ticket managed = ticketRepository.save(ticket);
				if(TicketStatus.OPEN.equals(managed.getStatus())) {
					eventPublisher.publishEvent(new TicketOpenedEvent(managed));
				} else if(TicketStatus.CLOSED.equals(managed.getStatus())) {
					eventPublisher.publishEvent(new TicketClosedEvent(managed));
				} 
				Broadcaster.broadcast(managed.toString());
				log.info("Persisted ticket {}", managed);
			});
	}
	
	private Ticket apply(Ticket ticket) {
		final Ticket persistentTicket = ticketRepository.findTopByStationIdAndDepartmentOrderByOpenTimestampDesc(
				ticket.getStation().getId(), ticket.getDepartment()).orElse(null);
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
	
	public Page<Ticket> findByDepartmentIdInAndOpenTimestampBetween(Set<Long> departmentIds, 
			Date from, Date to, Pageable pageable){
		return ticketRepository.findByDepartmentIdInAndOpenTimestampBetween(departmentIds, from, to, pageable);
	}
	
	public Page<Ticket> findByDepartmentIdInAndOpenTimestampBetweenAndStatus(Set<Long> departmentIds, Date from, Date to, 
			TicketStatus status, Pageable pageable){
		return ticketRepository.findByDepartmentIdInAndOpenTimestampBetweenAndStatus(departmentIds, from, to, 
				status, pageable);
	}
	
	public List<Ticket> findByDepartmentIdInAndOpenTimestampBetweenAndStatus(Set<Long> departmentIds, Date from, Date to, 
			TicketStatus status){
		return ticketRepository.findByDepartmentIdInAndOpenTimestampBetweenAndStatus(departmentIds, from, to, 
				status);
	}
	
	public List<Ticket> findByDepartmentIdInAndOpenTimestampBetween(Set<Long> departmentIds, Date from, Date to) {
		return ticketRepository.findByDepartmentIdInAndOpenTimestampBetween(departmentIds, from, to);
	}
	
	public List<Ticket> findByClosedTimestampAfterAndOpenTimestampBefore(Date from, Date to){
		return ticketRepository.findByClosedTimestampAfterAndOpenTimestampBefore(from, to);
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
