package com.revo.llms.report;

import java.util.List;
import java.util.Map;

import org.vaadin.addons.chartjs.ChartJs;
import org.vaadin.addons.chartjs.config.DonutChartConfig;
import org.vaadin.addons.chartjs.data.PieDataset;
import org.vaadin.addons.chartjs.options.Position;

import com.revo.llms.LlmsConstants;
import com.revo.llms.department.Department;
import com.revo.llms.ticket.Ticket;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TicketCountByDepartmentCard extends AbstractReportCard {
	private static final long serialVersionUID = -6191498638729089329L;

	private final ReportService reportService;

	@Override
	public void update(List<Ticket> tickets) {
		final Map<Department, Long> counts = reportService.getTotalTicketCountByDepartment(tickets);
		
		final DonutChartConfig config = new DonutChartConfig();
		final PieDataset dataset = new PieDataset();
		
		dataset.backgroundColor(LlmsConstants.COLORS);
		dataset.borderColor(LlmsConstants.COLORS);
		counts.forEach((department, count) -> dataset.addLabeledData(department.getName(), count.doubleValue()));
		config.data()
			.addDataset(dataset)
			.extractLabelsFromDataset(true).and()
			.options()
				.maintainAspectRatio(false)
				.title()
					.display(true)
					.position(Position.BOTTOM)
					.text("Total Tickets by Departments")
					.fontColor("white")
					.fullWidth(true).and()
				.legend()
					.display(true)
					.position(Position.LEFT).and()
				.done();
		
		final ChartJs chart = new ChartJs(config);
		chart.setWidth("500px");
		chart.setMinHeight("250px");
		add(chart);
	}
	
}
