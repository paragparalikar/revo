package com.revo.llms.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.vaadin.addons.chartjs.ChartJs;
import org.vaadin.addons.chartjs.config.BarChartConfig;
import org.vaadin.addons.chartjs.data.BarDataset;
import org.vaadin.addons.chartjs.options.Position;

import com.revo.llms.department.Department;
import com.revo.llms.department.DepartmentRepository;
import com.revo.llms.ticket.Ticket;
import com.revo.llms.ticket.TicketRepository;
import com.revo.llms.ticket.TicketStatus;

public class TodaysTicketStatusByDepartmentCard extends AbstractCard {
	private static final long serialVersionUID = -4307381280788824548L;

	private final TicketRepository ticketRepository;
	private final DepartmentRepository departmentRepository;
	
	public TodaysTicketStatusByDepartmentCard(
			TicketRepository ticketRepository, 
			DepartmentRepository departmentRepository) {
		this.ticketRepository = ticketRepository;
		this.departmentRepository = departmentRepository;
		update();
	}

	@Override
	public void update() {
		final Date midnight = getMidnightTime();
		final List<Ticket> tickets = ticketRepository.findByOpenTimestampAfter(midnight);
		
		final BarChartConfig config = new BarChartConfig();
		
		final BarDataset openDataset = new BarDataset();
		openDataset.backgroundColor("red");
		openDataset.borderColor("red");
		openDataset.label("Opened");
		
		final BarDataset closedDataset = new BarDataset();
		closedDataset.backgroundColor("green");
		closedDataset.borderColor("green");
		closedDataset.label("Closed");
		
		final List<String> labels = departmentRepository.findAll().stream()
				.map(Department::getName).collect(Collectors.toList());
		for(String label : labels) {
			final long openCount = tickets.stream()
				.filter(ticket -> TicketStatus.OPEN.equals(ticket.getStatus()))
				.map(Ticket::getDepartment)
				.map(Department::getName)
				.filter(Predicate.isEqual(label))
				.count();
			openDataset.addData((int)openCount);
			final long closeCount = tickets.stream()
					.filter(ticket -> TicketStatus.CLOSED.equals(ticket.getStatus()))
					.map(Ticket::getDepartment)
					.map(Department::getName)
					.filter(Predicate.isEqual(label))
					.count();
			closedDataset.addData((int)closeCount);
		}
		
		config.data()
			.labelsAsList(labels)
			.addDataset(closedDataset)
			.addDataset(openDataset).and()
			.options()
				.maintainAspectRatio(false)
				.title()
					.display(true)
					.position(Position.BOTTOM)
					.text("Today's Tickets by Departments")
					.fontColor("white")
					.fullWidth(true).and()
				.legend().display(true).and().done();
		
		final ChartJs chart = new ChartJs(config);
		chart.setWidth("450px");
		chart.setHeight("250px");
		add(chart);
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
