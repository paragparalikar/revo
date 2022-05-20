package com.revo.llms.user;

import static com.revo.llms.LlmsConstants.ROUTE_USERS;

import javax.annotation.security.PermitAll;

import org.springframework.security.core.userdetails.UserDetails;

import com.revo.llms.common.MainLayout;
import com.revo.llms.common.TitledGridView;
import com.revo.llms.common.security.SecurityService;
import com.revo.llms.department.DepartmentDataProvider;
import com.revo.llms.department.DepartmentService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PermitAll
@PageTitle("Users")
@Route(value = ROUTE_USERS, layout = MainLayout.class)
public class UserView extends TitledGridView<User> {
	private static final long serialVersionUID = 4494009539554472127L;

	private final UserService userService;
	private final SecurityService securityService;
	private final Grid<User> grid = new Grid<>();
	private final DataProvider<User, Void> dataProvider;
	private final UserEditor editor;
	
	public UserView(
			UserService userService, 
			SecurityService securityService,
			DepartmentService departmentService) {
		super(VaadinIcon.USERS.create(), "Users");
		this.userService = userService;
		this.securityService = securityService;
		this.dataProvider = new UserDataProvider(userService);
		grid.setItems(dataProvider);
		createColumns(grid);
		
		this.editor = new UserEditor(userService, dataProvider, new DepartmentDataProvider(departmentService));

		add(grid, editor);
	}
	
	@Override
	protected void createColumns(Grid<User> grid) {
		grid.addColumn(User::getUsername, "username").setHeader("Username");
		super.createColumns(grid);
	}
	
	@Override
	protected HorizontalLayout createActionCell(User value) {
		final HorizontalLayout container = super.createActionCell(value);
		final UserDetails userDetails = securityService.getAuthenticatedUser();
		container.setEnabled(!value.getUsername().equalsIgnoreCase(userDetails.getUsername()));
		return container;
	}

	@Override
	protected void create() {
		editor.open(null);
	}

	@Override
	protected void edit(User value) {
		final UserDetails userDetails = securityService.getAuthenticatedUser();
		if(value.getUsername().equalsIgnoreCase(userDetails.getUsername())) {
			setError("You can not edit your own profile !!!");
		} else {
			editor.open(value);
		}
	}

	@Override
	protected void delete(User value) {
		final UserDetails userDetails = securityService.getAuthenticatedUser();
		if(value.getUsername().equalsIgnoreCase(userDetails.getUsername())) {
			setError("You can not remove yourself !!!");
		} else {
			userService.delete(value);
			dataProvider.refreshAll();
		}
	}
	
}
