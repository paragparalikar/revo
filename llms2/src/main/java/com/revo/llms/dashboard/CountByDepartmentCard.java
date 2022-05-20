package com.revo.llms.dashboard;

import java.util.Map;

import org.vaadin.addons.chartjs.ChartJs;
import org.vaadin.addons.chartjs.config.DonutChartConfig;
import org.vaadin.addons.chartjs.data.PieDataset;
import org.vaadin.addons.chartjs.options.Position;

import com.revo.llms.department.Department;

public class CountByDepartmentCard extends AbstractCard {
	private static final long serialVersionUID = 2426736790179100496L;

	private final DashboardService dashboardService;
	
	public CountByDepartmentCard(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
		update();
	}
	
	@Override
	public void update() {
		final Map<Department, Long> counts = dashboardService.getOpenTicketCountByDepartment();
		
		final DonutChartConfig config = new DonutChartConfig();
		final PieDataset dataset = new PieDataset();
		dataset.backgroundColor("yellow", "purple", "blue", "red");
		dataset.borderColor("yellow", "purple", "blue", "red");
		counts.forEach((department, count) -> dataset.addLabeledData(department.getName(), count.doubleValue()));
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
