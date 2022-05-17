package com.revo.llms.common;

import com.revo.llms.dashboard.DashboardView;
import com.revo.llms.department.DepartmentView;
import com.revo.llms.product.ProductView;
import com.revo.llms.reason.ReasonView;
import com.revo.llms.ticket.TicketView;
import com.revo.llms.user.UserView;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;

public class Navigation extends VerticalLayout {
	private static final long serialVersionUID = 8891049443289300801L;

	public Navigation() {
		setHeightFull();
		final Tabs tabs = new Tabs();
		tabs.setOrientation(Tabs.Orientation.VERTICAL);
		tabs.addThemeVariants(TabsVariant.LUMO_SMALL);
		tabs.add(new Tab(VaadinIcon.DASHBOARD.create(), new RouterLink("Dashboard", DashboardView.class)));
		tabs.add(new Tab(VaadinIcon.TICKET.create(), new RouterLink("Tickets", TicketView.class)));
		
		tabs.add(new Tab(VaadinIcon.BUILDING.create(), new RouterLink("Departments", DepartmentView.class)));
		tabs.add(new Tab(VaadinIcon.EXCLAMATION_CIRCLE.create(), new RouterLink("Reason", ReasonView.class)));
		tabs.add(new Tab(VaadinIcon.CART.create(), new RouterLink("Products", ProductView.class)));
		tabs.add(new Tab(VaadinIcon.USERS.create(), new RouterLink("Users", UserView.class)));
		add(tabs);
	}

}
