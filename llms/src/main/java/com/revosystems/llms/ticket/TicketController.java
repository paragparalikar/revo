package com.revosystems.llms.ticket;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.revosystems.llms.Department;
import com.revosystems.llms.SecurityUtils;
import com.revosystems.llms.reason.Reason;
import com.revosystems.llms.reason.ReasonService;

@RestController
@RequestMapping("/tickets")
public class TicketController {

	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private ReasonService reasonService;
	
	@GetMapping
	public Page<Ticket> findAll(@AuthenticationPrincipal User user, 
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(defaultValue = "ASC") Direction direction, 
			@RequestParam(defaultValue = "openTimestamp") String... properties){
		final Set<Department> departments = SecurityUtils.getAccessibleDepartments(user);
		final Pageable pageable = PageRequest.of(page, size, direction, properties);
		return ticketService.findAllByDepartments(departments, pageable);
	}
	
	@PatchMapping
	public Ticket updateReason(Long ticketId, Long reasonId) {
		final Ticket ticket = ticketService.findById(ticketId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No ticket found with id " + ticketId));
		final Reason reason = reasonService.findById(reasonId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No reason found with id " + reasonId));
		ticket.setReason(reason);
		return ticketService.save(ticket);
	}
	
}
