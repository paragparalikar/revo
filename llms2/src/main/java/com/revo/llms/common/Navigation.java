package com.revo.llms.common;

import static com.revo.llms.LlmsConstants.PREFIX_PAGE;
import static com.revo.llms.LlmsConstants.ROUTE_CATEGORIES;
import static com.revo.llms.LlmsConstants.ROUTE_DASHBOARD;
import static com.revo.llms.LlmsConstants.ROUTE_DEPARTMENTS;
import static com.revo.llms.LlmsConstants.ROUTE_PRODUCTS;
import static com.revo.llms.LlmsConstants.ROUTE_REASONS;
import static com.revo.llms.LlmsConstants.ROUTE_REPORTS;
import static com.revo.llms.LlmsConstants.ROUTE_STATIONS;
import static com.revo.llms.LlmsConstants.ROUTE_TICKETS;
import static com.revo.llms.LlmsConstants.ROUTE_USERS;
import static com.vaadin.flow.component.icon.VaadinIcon.BAR_CHART_H;
import static com.vaadin.flow.component.icon.VaadinIcon.BUILDING;
import static com.vaadin.flow.component.icon.VaadinIcon.CART;
import static com.vaadin.flow.component.icon.VaadinIcon.DASHBOARD;
import static com.vaadin.flow.component.icon.VaadinIcon.EXCLAMATION_CIRCLE;
import static com.vaadin.flow.component.icon.VaadinIcon.SPLIT;
import static com.vaadin.flow.component.icon.VaadinIcon.TICKET;
import static com.vaadin.flow.component.icon.VaadinIcon.USERS;
import static com.vaadin.flow.component.icon.VaadinIcon.WRENCH;
import static com.vaadin.flow.component.tabs.TabsVariant.LUMO_SMALL;

import org.springframework.security.core.userdetails.UserDetails;

import com.revo.llms.category.CategoryView;
import com.revo.llms.common.security.SecurityService;
import com.revo.llms.dashboard.DashboardView;
import com.revo.llms.department.DepartmentView;
import com.revo.llms.product.ProductView;
import com.revo.llms.reason.ReasonView;
import com.revo.llms.report.ReportView;
import com.revo.llms.station.StationView;
import com.revo.llms.ticket.TicketView;
import com.revo.llms.user.UserView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouterLink;

public class Navigation extends VerticalLayout implements AfterNavigationObserver {
	private static final long serialVersionUID = 8891049443289300801L;

	private final Label usernameLabel;
	private final SecurityService securityService;
	private final Tab dashboardTab = new Tab(DASHBOARD.create(), new RouterLink("Dashboard", DashboardView.class));
	private final Tab ticketTab = new Tab(TICKET.create(), new RouterLink("Tickets", TicketView.class));
	private final Tab departmentTab = new Tab(BUILDING.create(), new RouterLink("Departments", DepartmentView.class));
	private final Tab reasonTab = new Tab(EXCLAMATION_CIRCLE.create(), new RouterLink("Reason", ReasonView.class));
	private final Tab productTab = new Tab(CART.create(), new RouterLink("Products", ProductView.class));
	private final Tab userTab = new Tab(USERS.create(), new RouterLink("Users", UserView.class));
	private final Tab reportTab = new Tab(BAR_CHART_H.create(), new RouterLink("Reports", ReportView.class));
	private final Tab stationTab = new Tab(WRENCH.create(), new RouterLink("Stations", StationView.class));
	private final Tab categoryTab = new Tab(SPLIT.create(), new RouterLink("Reason Categories", CategoryView.class));
	private final Tabs tabs = new Tabs();
	
	public Navigation(SecurityService securityService, final Label usernameLabel) {
		this.usernameLabel = usernameLabel;
		this.securityService = securityService;
		setHeightFull();
		add(createLogo(), tabs, createFooter());
		expand(tabs);
		tabs.setOrientation(Tabs.Orientation.VERTICAL);
		tabs.addThemeVariants(LUMO_SMALL);
	}
	
	private Component createLogo() {
		final Image image = new Image("images/cnh-logo.png", "CNH");
		image.setWidthFull();
		return image;
	}
	
	private Component createFooter() {
		final Image image = new Image("images/logo-revo.png", "Revo");
		image.setWidthFull();
		return image;
	}
	
	private void init() {
		final UserDetails userDetails = securityService.getAuthenticatedUser();
		if(0 == tabs.getComponentCount() && null != userDetails) {
			usernameLabel.setText("Welcome, " + userDetails.getUsername());
			if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_DASHBOARD)) tabs.add(dashboardTab);
			if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_REPORTS)) tabs.add(reportTab);
			if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_TICKETS)) tabs.add(ticketTab);
			if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_DEPARTMENTS)) tabs.add(departmentTab);
			if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_STATIONS)) tabs.add(stationTab);
			if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_CATEGORIES)) tabs.add(categoryTab);
			if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_REASONS)) tabs.add(reasonTab);
			if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_PRODUCTS)) tabs.add(productTab);
			if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_USERS)) tabs.add(userTab);
		}
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		init();
		final String segment = event.getLocation().getFirstSegment();
		switch(segment) {
		case ROUTE_DASHBOARD : if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_DASHBOARD)) tabs.setSelectedTab(dashboardTab); break;
		case ROUTE_REPORTS : if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_REPORTS)) tabs.setSelectedTab(reportTab); break;
		case ROUTE_TICKETS: if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_TICKETS)) tabs.setSelectedTab(ticketTab); break;
		case ROUTE_DEPARTMENTS: if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_DEPARTMENTS)) tabs.setSelectedTab(departmentTab); break;
		case ROUTE_STATIONS: if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_STATIONS)) tabs.setSelectedTab(stationTab); break;
		case ROUTE_CATEGORIES : if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_CATEGORIES)) tabs.setSelectedTab(categoryTab); break;
		case ROUTE_REASONS: if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_REASONS)) tabs.setSelectedTab(reasonTab); break;
		case ROUTE_PRODUCTS: if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_PRODUCTS)) tabs.setSelectedTab(productTab); break;
		case ROUTE_USERS: if(securityService.hasAuthority(PREFIX_PAGE + ROUTE_USERS))tabs.setSelectedTab(userTab); break;
		}
	}

}
