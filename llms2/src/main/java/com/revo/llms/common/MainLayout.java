package com.revo.llms.common;

import org.springframework.beans.factory.annotation.Autowired;

import com.revo.llms.common.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MainLayout extends AppLayout {
	private static final long serialVersionUID = -6154652183060289797L;

	private final DrawerToggle toggle = new DrawerToggle();
	private final H1 title = new H1("Line Loss Monitoring System");
	private final Label usernameLabel = new Label();
	private final Button logoutButton = new Button("Logout", VaadinIcon.SIGN_OUT.create());
	private final HorizontalLayout header = new HorizontalLayout(toggle, title, usernameLabel, logoutButton);
	
	public MainLayout(@Autowired SecurityService securityService) {
		title.getStyle()
			.set("font-size", "var(--lumo-font-size-l)")
			.set("margin", "0");
		header.expand(title); 
		header.setWidthFull();
		header.setAlignItems(Alignment.CENTER);
		logoutButton.addClickListener(event -> securityService.logout());
        
		getElement().getStyle().set("height", "100%");
		setPrimarySection(Section.DRAWER);
		addToDrawer(new Navigation(securityService, usernameLabel));
		addToNavbar(header);
	}
	
}
