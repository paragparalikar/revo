package com.revo.llms.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.vaadin.addons.chartjs.ChartJs;
import org.vaadin.addons.chartjs.config.BarChartConfig;
import org.vaadin.addons.chartjs.data.BarDataset;
import org.vaadin.addons.chartjs.options.Position;

import com.revo.llms.department.Department;
import com.revo.llms.ticket.TicketStatus;

public class TodaysTicketStatusByDepartmentCard extends AbstractCard {
	private static final long serialVersionUID = -4307381280788824548L;

	private final DashboardService dashboardService;
	
	public TodaysTicketStatusByDepartmentCard(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
		update();
	}

	@Override
	public void update() {
		
		final Map<Department, Map<TicketStatus, Long>> data = dashboardService.getTodaysTicketCountByDepartmentByStatus();
		
		final BarChartConfig config = new BarChartConfig();
		
		final BarDataset openDataset = new BarDataset();
		openDataset.backgroundColor("red");
		openDataset.borderColor("red");
		openDataset.label("Opened");
		
		final BarDataset closedDataset = new BarDataset();
		closedDataset.backgroundColor("green");
		closedDataset.borderColor("green");
		closedDataset.label("Closed");
		
		final List<String> labels = new ArrayList<>(data.size());
		for(Department department : data.keySet()) {
			labels.add(department.getName());
			final Map<TicketStatus, Long> statusData = data.get(department);
			openDataset.addData(statusData.getOrDefault(TicketStatus.OPEN, 0l).intValue());
			closedDataset.addData(statusData.getOrDefault(TicketStatus.CLOSED, 0l).intValue());
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
		chart.setWidth("500px");
		chart.setHeight("250px");
		add(chart);
	}
	
}
