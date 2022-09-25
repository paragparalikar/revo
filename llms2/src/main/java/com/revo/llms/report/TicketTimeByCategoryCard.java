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
import com.revo.llms.category.Category;
import com.revo.llms.ticket.Ticket;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TicketTimeByCategoryCard extends AbstractReportCard {
	private static final long serialVersionUID = 5706710352098415494L;

	private final ReportService reportService;
	
	@Override
	public void update(List<Ticket> tickets) {
		final Map<Category, Long> data = reportService.getTicketTimeByCategory(tickets);
		final BarDataset dataset = new BarDataset();
		final BarChartConfig config = new BarChartConfig();
		final ChartJs chart = new ChartJs(config);
		
		dataset.backgroundColor(LlmsConstants.COLORS);
		dataset.borderColor(LlmsConstants.COLORS);
		final List<Double> dataItems = new ArrayList<>(data.size());
		final List<String> labels = new ArrayList<>(data.size());
		data.forEach((category, hours) -> {
			labels.add(null == category ? "None" : category.getName());
			dataItems.add(hours.doubleValue());
		});
		dataset.dataAsList(dataItems);
		config
			.horizontal()
			.data()
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
					.text("Ticket Time By Reason Category").and()
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
