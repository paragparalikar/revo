package com.revo.llms.user;

import javax.annotation.security.PermitAll;

import com.revo.llms.LlmsConstants;
import com.revo.llms.common.MainLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PermitAll
@PageTitle("Users")
@Route(value = LlmsConstants.ROUTE_USERS, layout = MainLayout.class)
public class UserView extends VerticalLayout {
	private static final long serialVersionUID = 4494009539554472127L;

	public UserView() {
		add(new Label("This is users view"));
	}
	
}
