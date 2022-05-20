package com.revo.llms.report;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revo.llms.department.Department;
import com.revo.llms.department.DepartmentService;
import com.revo.llms.ticket.Ticket;
import com.revo.llms.ticket.TicketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

	private final TicketService ticketService;
	private final DepartmentService departmentService;
	
	public List<Ticket> findByClosedTimestampAfterAndOpenTimestampBefore(Date from, Date to){
		return ticketService.findByClosedTimestampAfterAndOpenTimestampBefore(from, to);
	}
	
	public Map<Department, Long> getTotalTicketCountByDepartment(List<Ticket> tickets){
		return tickets.stream()
				.map(Ticket::getDepartment)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}
	
}
