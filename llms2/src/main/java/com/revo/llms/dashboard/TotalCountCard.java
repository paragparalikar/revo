package com.revo.llms.dashboard;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TotalCountCard extends AbstractCard {
	private static final long serialVersionUID = -7936992042674510443L;

	private final DashboardService dashboardService;
	private final Label countLabel = new Label();
	private final Label descriptionLabel = new Label("Total Open Tickets");
	private final VerticalLayout container = new VerticalLayout(countLabel, descriptionLabel);
	
	public TotalCountCard(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
		countLabel.getStyle().set("font-size", "100px");
		countLabel.getStyle().set("text-align", "center");
		countLabel.setWidthFull();
		container.setSizeFull();
		container.setAlignItems(Alignment.CENTER);
		add(container);
		update();
	}
	
	@Override
	public void update() {
		countLabel.setText(String.valueOf(dashboardService.getOpenTicketCount()));
	}
	
}
