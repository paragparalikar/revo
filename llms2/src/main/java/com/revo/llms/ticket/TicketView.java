package com.revo.llms.ticket;

import com.revo.llms.common.MainLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Tickets")
@Route(value = "tickets", layout = MainLayout.class)
public class TicketView extends VerticalLayout {
	private static final long serialVersionUID = 821057894670434504L;

	public TicketView() {
		add(new Label("This is tickets view"));
	}
	
}
