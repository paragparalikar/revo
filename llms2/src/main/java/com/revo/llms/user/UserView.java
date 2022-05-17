package com.revo.llms.user;

import com.revo.llms.common.MainLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Users")
@Route(value = "users", layout = MainLayout.class)
public class UserView extends VerticalLayout {
	private static final long serialVersionUID = 4494009539554472127L;

	public UserView() {
		add(new Label("This is users view"));
	}
	
}
