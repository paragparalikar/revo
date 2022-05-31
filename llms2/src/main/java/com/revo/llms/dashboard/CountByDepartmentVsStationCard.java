package com.revo.llms.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.vaadin.addons.chartjs.ChartJs;
import org.vaadin.addons.chartjs.config.BubbleChartConfig;
import org.vaadin.addons.chartjs.data.BubbleData;
import org.vaadin.addons.chartjs.data.BubbleDataset;
import org.vaadin.addons.chartjs.options.Position;
import org.vaadin.addons.chartjs.options.scale.Axis;
import org.vaadin.addons.chartjs.options.scale.CategoryScale;
import org.vaadin.addons.chartjs.options.scale.DefaultScale;

import com.revo.llms.department.Department;

public class CountByDepartmentVsStationCard extends AbstractCard {
	private static final long serialVersionUID = -5993667504351787131L;

	private final DashboardService dashboardService;
	
	public CountByDepartmentVsStationCard(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
		update();
	}
	
	@Override
	public void update() {
		final Map<Integer, Map<Department, Long>> data = 
				dashboardService.getTodaysOpenTicketCountByDepartmentByStation();
		
		final BubbleChartConfig config = new BubbleChartConfig();
		final BubbleDataset dataset = new BubbleDataset();
		
		final List<String> dataLabels = new ArrayList<>();
		final List<BubbleData> dataItems = new ArrayList<>();
		for(Integer stationId : data.keySet()) {
			final Map<Department, Long> counts = data.get(stationId);
			for(Department department : counts.keySet()) {
				final BubbleData bubble = new BubbleData();
				bubble.x(stationId.doubleValue());
				bubble.y(department.getId().doubleValue());
				final Long count = counts.getOrDefault(department, 0l);
				bubble.r(count.doubleValue());
				dataItems.add(bubble);
				//final String label = department.getName() + ":" + count;
			}
			dataLabels.add(String.valueOf(stationId));
		}
		
		dataset.dataAsList(dataItems);
		dataset.backgroundColor("rgba(255, 100, 100, 0.2)");
		dataset.borderColor("rgba(255, 100, 200, 0.2)");
		
		config.data()
			.addDataset(dataset)
			.labelsAsList(dataLabels)
			.extractLabelsFromDataset(false).and()
			.options()
				.title()
					.text("Open Tickets for Station and Department")
					.fontColor("white")
					.display(true)
					.position(Position.BOTTOM)
					.fullWidth(true).and()
				.maintainAspectRatio(false)
				.legend().display(false).and()
				.scales()
					.add(Axis.X, new CategoryScale()
							.display(true)
							.scaleLabel()
								.display(true)
								.labelString("Stations").and())
					.add(Axis.Y, new DefaultScale()
							.display(true)
							.scaleLabel()
								.display(true)
								.labelString("Departments").and())	
				.and().done();
		
		final ChartJs chart = new ChartJs(config);
		getContent().removeAll();
		chart.setWidth("1000px");
		chart.setHeight("250px");
		add(chart);
	}

}
