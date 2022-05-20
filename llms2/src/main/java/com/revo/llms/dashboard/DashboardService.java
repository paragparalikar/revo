package com.revo.llms.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revo.llms.department.Department;
import com.revo.llms.department.DepartmentService;
import com.revo.llms.ticket.Ticket;
import com.revo.llms.ticket.TicketService;
import com.revo.llms.ticket.TicketStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
	
	private final TicketService ticketService;
	private final DepartmentService departmentService;
	
	public long getOpenTicketCount() {
		return ticketService.countByStatus(TicketStatus.OPEN);
	}
	
	public Map<Department, Long> getOpenTicketCountByDepartment(){
		final List<Ticket> tickets = ticketService.findByStatus(TicketStatus.OPEN);
		return tickets.stream()
				.map(Ticket::getDepartment)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}
	
	public Map<Integer, Map<Department, Long>> getTodaysOpenTicketCountByDepartmentByStation(){
		final List<Department> departments = departmentService.findAll();
		final List<Ticket> tickets = ticketService.findByStatus(TicketStatus.OPEN);
		final Map<Integer, Map<Department, Long>> result = new HashMap<>(30);
		for(int stationId = 1; stationId <= 30; stationId++) {
			final Map<Department, Long> counts = new HashMap<>(departments.size());
			for(Department department : departments) {
				final Integer localStationId = Integer.valueOf(stationId);
				final Long count = tickets.stream()
						.filter(ticket -> Objects.equals(department, ticket.getDepartment()))
						.filter(ticket -> Objects.equals(localStationId, ticket.getStationId()))
						.count();
				counts.put(department, count);
			}
			result.put(stationId, counts);
		}
		return result;
	}
	
	public Map<Department, Map<TicketStatus, Long>> getTodaysTicketCountByDepartmentByStatus(){
		final List<Department> departments = departmentService.findAll();
		final List<Ticket> tickets = ticketService.findByOpenTimestampAfter(getMidnightTime());
		final Map<Department, Map<TicketStatus, Long>> result = new HashMap<>(departments.size());
		for(Department department : departments) {
			final Map<TicketStatus, Long> counts = new EnumMap<>(TicketStatus.class);
			for(TicketStatus status : TicketStatus.values()) {
				final Long count = tickets.stream()
					.filter(ticket -> Objects.equals(department, ticket.getDepartment()))
					.filter(ticket -> Objects.equals(status, ticket.getStatus()))
					.count();
				counts.put(status, count);
			}
			result.put(department, counts);
		}
		return result;
	}
	
	private Date getMidnightTime() {
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}

}
