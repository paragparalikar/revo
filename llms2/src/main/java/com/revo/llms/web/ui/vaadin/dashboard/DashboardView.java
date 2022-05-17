package com.revo.llms.web.ui.vaadin.dashboard;

import com.revo.llms.web.ui.vaadin.common.MainLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("Dashboard")
@Route(value = "dashboards", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {
	private static final long serialVersionUID = -9207529279894406046L;

	public DashboardView() {
		add(new Label("This is dashboard"));
	}
	
}
