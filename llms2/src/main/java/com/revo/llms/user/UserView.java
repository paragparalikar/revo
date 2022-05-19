package com.revo.llms.user;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;

import com.revo.llms.LlmsConstants;
import com.revo.llms.common.MainLayout;
import com.revo.llms.common.TitledGridView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PermitAll
@PageTitle("Users")
@Route(value = LlmsConstants.ROUTE_USERS, layout = MainLayout.class)
public class UserView extends TitledGridView<User> {
	private static final long serialVersionUID = 4494009539554472127L;

	private final UserService userService;
	private final Grid<User> grid = new Grid<>();
	
	public UserView(@Autowired UserService userService) {
		super(VaadinIcon.USERS.create(), "Users");
		this.userService = userService;
	}

	@Override
	protected void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void edit(User value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void delete(User value) {
		// TODO Auto-generated method stub
		
	}
	
}
