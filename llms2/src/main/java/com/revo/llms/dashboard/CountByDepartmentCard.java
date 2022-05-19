package com.revo.llms.dashboard;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.vaadin.addons.chartjs.ChartJs;
import org.vaadin.addons.chartjs.config.DonutChartConfig;
import org.vaadin.addons.chartjs.data.PieDataset;
import org.vaadin.addons.chartjs.options.Position;

import com.revo.llms.department.Department;
import com.revo.llms.ticket.Ticket;
import com.revo.llms.ticket.TicketRepository;
import com.revo.llms.ticket.TicketStatus;

public class CountByDepartmentCard extends AbstractCard {
	private static final long serialVersionUID = 2426736790179100496L;

	private final TicketRepository ticketRepository;
	
	public CountByDepartmentCard(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
		update();
	}
	
	@Override
	public void update() {
		final List<Ticket> tickets = ticketRepository.findByStatus(TicketStatus.OPEN);
		final Map<String, Long> counts = tickets.stream()
				.map(Ticket::getDepartment)
				.map(Department::getName)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		
		final DonutChartConfig config = new DonutChartConfig();
		final PieDataset dataset = new PieDataset();
		dataset.backgroundColor("yellow", "purple", "blue", "red");
		dataset.borderColor("yellow", "purple", "blue", "red");
		counts.forEach((name, count) -> dataset.addLabeledData(name, count.doubleValue()));
		config.data()
			.addDataset(dataset)
			.extractLabelsFromDataset(true)
			.and().options()
				.title()
					.display(true)
					.position(Position.BOTTOM)
					.text("Open Tickets by Departments")
					.fontColor("white")
					.fullWidth(true).and().done();
		final ChartJs chart = new ChartJs(config);
		chart.setWidthFull();
		add(chart);
	}

}
