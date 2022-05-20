package com.revo.llms.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.vaadin.addons.chartjs.ChartJs;
import org.vaadin.addons.chartjs.config.BarChartConfig;
import org.vaadin.addons.chartjs.data.BarDataset;
import org.vaadin.addons.chartjs.options.scale.Axis;
import org.vaadin.addons.chartjs.options.scale.CategoryScale;
import org.vaadin.addons.chartjs.options.scale.DefaultScale;

import com.revo.llms.LlmsConstants;
import com.revo.llms.department.Department;
import com.revo.llms.ticket.Ticket;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TicketTimeByDepartmentCard extends AbstractReportCard {
	private static final long serialVersionUID = -7395996931307750310L;

	private final ReportService reportService;
	
	@Override
	public void update(List<Ticket> tickets) {
		final Map<Department, Long> data = reportService.getTicketTimeByDepartment(tickets);
		final BarChartConfig config = new BarChartConfig();
		final BarDataset dataset = new BarDataset();
		final ChartJs chart = new ChartJs(config);
		
		dataset.backgroundColor(LlmsConstants.COLORS);
		dataset.borderColor(LlmsConstants.COLORS);
		final List<Double> dataItems = new ArrayList<>(data.size());
		final List<String> labels = new ArrayList<>(data.size());
		data.forEach((department, minutes) -> {
			labels.add(department.getName());
			dataItems.add(minutes.doubleValue());
		});
		dataset.dataAsList(dataItems);
		config
			.horizontal()
			.data().clear()
				.addDataset(dataset)
				.labelsAsList(labels)
				.extractLabelsFromDataset(false).and()
			.options()
				.maintainAspectRatio(false)
				.legend()
					.display(false).and()
				.title()
					.display(true)
					.fullWidth(true)
					.fontColor("white")
					.text("Ticket Time By Department").and()
				.scales()
					.add(Axis.Y, new CategoryScale()
							.display(true)
								.ticks()
									.autoSkip(false).and())
					.add(Axis.X, new DefaultScale()
							.display(true)
							.scaleLabel()
								.display(true)
								.labelString("Hours").and()).and()
				.done();
		getContent().removeAll();
		chart.setWidth("500px");
		chart.setHeightFull();
		add(chart);
	}

}
