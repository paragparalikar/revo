package com.revo.llms.report;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revo.llms.category.Category;
import com.revo.llms.department.Department;
import com.revo.llms.reason.Reason;
import com.revo.llms.ticket.Ticket;
import com.revo.llms.ticket.TicketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

	private final TicketService ticketService;
	
	public List<Ticket> findByClosedTimestampAfterAndOpenTimestampBefore(Date from, Date to){
		return ticketService.findByClosedTimestampAfterAndOpenTimestampBefore(from, to);
	}
	
	public Map<Department, Long> getTotalTicketCountByDepartment(List<Ticket> tickets){
		return tickets.stream()
				.map(Ticket::getDepartment)
				.filter(Objects::nonNull)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}
	
	public Map<Reason, Long> getTotalTicketCountByReason(List<Ticket> tickets){
		return tickets.stream()
				.map(Ticket::getReason)
				.filter(Objects::nonNull)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}
	
	public Map<Category, Long> getTotalTicketCountByCategory(List<Ticket> tickets){
		return tickets.stream()
				.map(Ticket::getReason)
				.filter(Objects::nonNull)
				.map(Reason::getCategory)
				.filter(Objects::nonNull)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}
	
	public Map<Reason, Long> getTicketTimeByReason(List<Ticket> tickets){
		return tickets.stream()
				.filter(ticket -> Objects.nonNull(ticket.getReason()))
				.collect(Collectors.groupingBy(Ticket::getReason, 
						Collectors.mapping(ticket -> {
							final Date open = ticket.getOpenTimestamp();
							final Date close = null == ticket.getClosedTimestamp() ? new Date() : ticket.getClosedTimestamp();
							return Duration.ofMillis(close.getTime() - open.getTime()).toHours();
						}, Collectors.summingLong(Long::longValue))));
	}
	
	public Map<Category, Long> getTicketTimeByCategory(List<Ticket> tickets){
		return tickets.stream()
				.filter(ticket -> Objects.nonNull(ticket.getReason()))
				.filter(ticket -> Objects.nonNull(ticket.getReason().getCategory()))
				.collect(Collectors.groupingBy(ticket -> ticket.getReason().getCategory(), 
						Collectors.mapping(ticket -> {
							final Date open = ticket.getOpenTimestamp();
							final Date close = null == ticket.getClosedTimestamp() ? new Date() : ticket.getClosedTimestamp();
							return Duration.ofMillis(close.getTime() - open.getTime()).toHours();
						}, Collectors.summingLong(Long::longValue))));
	}
	
	public Map<Department, Long> getTicketTimeByDepartment(List<Ticket> tickets){
		return tickets.stream()
				.filter(ticket -> Objects.nonNull(ticket.getDepartment()))
				.collect(Collectors.groupingBy(Ticket::getDepartment, 
						Collectors.mapping(ticket -> {
							final Date open = ticket.getOpenTimestamp();
							final Date close = null == ticket.getClosedTimestamp() ? new Date() : ticket.getClosedTimestamp();
							return Duration.ofMillis(close.getTime() - open.getTime()).toHours();
						}, Collectors.summingLong(Long::longValue))));
	}
}
