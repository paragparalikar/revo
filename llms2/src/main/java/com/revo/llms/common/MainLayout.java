package com.revo.llms.common;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;

public class MainLayout extends AppLayout {
	private static final long serialVersionUID = -6154652183060289797L;

	private final DrawerToggle toggle = new DrawerToggle();
	private final H1 title = new H1("Line Loss Monitoring System");
	
	public MainLayout() {
		title.getStyle()
			.set("font-size", "var(--lumo-font-size-l)")
			.set("margin", "0");
		getElement().getStyle().set("height", "100%");
		setPrimarySection(Section.NAVBAR);
		addToDrawer(new Navigation());
		addToNavbar(toggle, title);
	}
}
