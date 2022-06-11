package com.revo.llms.ticket;


import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fazecast.jSerialComm.SerialPort;
import com.revo.llms.port.PortPoller;
import com.revo.llms.port.PortResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TicketService {
	
	@Value("${llms.port-name:USB}")
	private String portName;
	
	@Value("${llms.port.request-timeout:1000}")
	private int requestTimeoutMillis;
	
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
				.portResolver(portResolver)
				.build();
	}
	
	@Scheduled(initialDelayString = "${poll.initial-delay.millis:1000}", 
			fixedDelayString = "${poll.fixed-delay.millis:5000}")
	public void poll() {
		poll(new byte[]{1,3,0,0,0,15,5,(byte)206});
		poll(new byte[]{2,3,0,0,0,15,5,(byte)253});
	}
	
	private void poll(byte[] request) {
		Optional.ofNullable(portPoller.poll(request))
			.map(ticketsResolver::apply)
			.orElse(Collections.emptySet()).stream()
			.map(this::apply)
			.filter(Objects::nonNull)
			.map(ticketRepository::save)
			.map(Object::toString)
			.forEach(log::info);
	}
	
	public static void main(String[] args) {
		final byte[] buffer = new byte[35];
		final byte[] request1 = new byte[]{1,3,0,0,0,15,5,(byte)206};
		final byte[] request2 = new byte[]{2,3,0,0,0,15,5,(byte)253};
		final PortResolver portResolver = new PortResolver();
		final SerialPort port = portResolver.resolve("USB");
		port.openPort();
		port.setBaudRate(9600);
		port.setNumDataBits(8);
		port.setNumStopBits(1);
		port.setParity(SerialPort.NO_PARITY);
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 5000, 5000);

		port.writeBytes(request1, request1.length);
		port.readBytes(buffer, 35);
		System.out.println(DatatypeConverter.printHexBinary(request1) +  " : "+ DatatypeConverter.printHexBinary(buffer));
		
		port.writeBytes(request2, request2.length);
		port.readBytes(buffer, 35);
		System.out.println(DatatypeConverter.printHexBinary(request2) +  " : "+ DatatypeConverter.printHexBinary(buffer));
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
