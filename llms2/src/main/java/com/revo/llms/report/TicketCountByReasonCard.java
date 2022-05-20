package com.revo.llms.report;

import java.util.List;
import java.util.Map;

import org.vaadin.addons.chartjs.ChartJs;
import org.vaadin.addons.chartjs.config.DonutChartConfig;
import org.vaadin.addons.chartjs.data.PieDataset;
import org.vaadin.addons.chartjs.options.Position;

import com.revo.llms.LlmsConstants;
import com.revo.llms.reason.Reason;
import com.revo.llms.ticket.Ticket;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TicketCountByReasonCard extends AbstractReportCard {
	private static final long serialVersionUID = 1746593911626591253L;

	private final ReportService reportService;
	
	@Override
	public void update(List<Ticket> tickets) {
		final Map<Reason, Long> counts = reportService.getTotalTicketCountByReason(tickets);
		final DonutChartConfig config = new DonutChartConfig();
		final PieDataset dataset = new PieDataset();
		final ChartJs chart = new ChartJs(config);
		
		dataset.backgroundColor(LlmsConstants.COLORS);
		dataset.borderColor(LlmsConstants.COLORS);
		counts.forEach((reason, count) -> {
			final String text = null == reason ? "None" : reason.getText();
			dataset.addLabeledData(text, count.doubleValue());
		});
		config.data().clear()
			.addDataset(dataset)
			.extractLabelsFromDataset(true).and()
			.options()
				.maintainAspectRatio(false)
				.title()
					.display(true)
					.position(Position.BOTTOM)
					.text("Total Tickets by Reasons")
					.fontColor("white")
					.fullWidth(true).and()
				.legend()
					.display(true)
					.position(Position.LEFT).and()
				.done();
		
		getContent().removeAll();
		chart.setWidth("500px");
		chart.setHeightFull();
		add(chart);
	}

}
