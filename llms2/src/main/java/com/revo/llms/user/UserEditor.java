package com.revo.llms.user;

import static com.revo.llms.LlmsConstants.ROUTE_CATEGORIES;
import static com.revo.llms.LlmsConstants.ROUTE_DASHBOARD;
import static com.revo.llms.LlmsConstants.ROUTE_DEPARTMENTS;
import static com.revo.llms.LlmsConstants.ROUTE_PARTS;
import static com.revo.llms.LlmsConstants.ROUTE_PRODUCTS;
import static com.revo.llms.LlmsConstants.ROUTE_REASONS;
import static com.revo.llms.LlmsConstants.ROUTE_REPORTS;
import static com.revo.llms.LlmsConstants.ROUTE_STATIONS;
import static com.revo.llms.LlmsConstants.ROUTE_TICKETS;
import static com.revo.llms.LlmsConstants.ROUTE_USERS;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.revo.llms.common.TitledFormEditor;
import com.revo.llms.department.Department;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;

public class UserEditor extends TitledFormEditor<User> {
	private static final long serialVersionUID = -8504715148318995250L;

	private final Tabs tabs = new Tabs();
	private final UserService userService;
	private final DataProvider<User, Void> userDataProvider;
	private final DataProvider<Department, Void> departmentDataProvider;
	private final Map<Tab, FormLayout> tabComponentMap = new LinkedHashMap<>();
	
	public UserEditor(UserService userSerivce, 
			DataProvider<User, Void> dataProvider,
			DataProvider<Department, Void> departmentDataProvider) {
		super(VaadinIcon.USER.create(), "User", User::new);
		this.userService = userSerivce;
		this.userDataProvider = dataProvider;
		this.departmentDataProvider = departmentDataProvider;
		tabs.addSelectedChangeListener(event -> {
			tabComponentMap.values().forEach(form -> form.setVisible(false));
			Optional.ofNullable(tabComponentMap.get(event.getSelectedTab())).ifPresent(form -> form.setVisible(true));
		});
		createForm(getBinder(), getForm());
	}
	
	private void createForm(Binder<User> binder, FormLayout form) {
		final FormLayout subForm = new FormLayout();
		final VerticalLayout container = new VerticalLayout(tabs, subForm);
		form.add(container);
		createCredentialsTab(binder, subForm);
		createDepartmentsTab(binder, subForm);
		createPagesTab(binder, subForm);
	}
	
	private void createCredentialsTab(Binder<User> binder, FormLayout form) {
		final TextField usernameField = new TextField("Username");
		binder.forField(usernameField).asRequired("Username is required")
			.withValidator(this::isUsernameValid, "This username is already taken")
			.bind(User::getUsername, User::setUsername);
		final PasswordField passwordField = new PasswordField("Password");
		binder.forField(passwordField).asRequired("Password is required")
			.bind(User::getPassword, User::setPassword);
		final Tab tab = new Tab(VaadinIcon.USER.create(), new Label("Credentials"));
		final FormLayout tabForm = new FormLayout(usernameField, passwordField);
		tabComponentMap.put(tab, tabForm);
		form.add(tabForm);
		tabs.add(tab);
	}
	
	private void createDepartmentsTab(Binder<User> binder, FormLayout form) {
		final CheckboxGroup<Department> group = new CheckboxGroup<>();
		group.setItemLabelGenerator(Department::getName);
		group.setItems(departmentDataProvider);
		binder.forField(group).bind(User::getDepartments, User::setDepartments);
		group.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
		final Tab tab = new Tab(VaadinIcon.BUILDING.create(), new Label("Departments"));
		final FormLayout tabForm = new FormLayout(group);
		tabComponentMap.put(tab, tabForm);
		form.add(tabForm);
		tabs.add(tab);
		tabForm.setVisible(false);
	}
	
	private void createPagesTab(Binder<User> binder, FormLayout form) {
		final CheckboxGroup<String> group = new CheckboxGroup<>();
		group.setItems(ROUTE_DASHBOARD, ROUTE_DEPARTMENTS, ROUTE_PARTS,
				ROUTE_PRODUCTS, ROUTE_REASONS, ROUTE_TICKETS, 
				ROUTE_CATEGORIES, ROUTE_STATIONS,
				ROUTE_USERS, ROUTE_REPORTS);
		binder.forField(group).bind(User::getPages, User::setPages);
		group.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
		final Tab tab = new Tab(VaadinIcon.FILE_O.create(), new Label("Pages"));
		final FormLayout tabForm = new FormLayout(group);
		tabComponentMap.put(tab, tabForm);
		form.add(tabForm);
		tabs.add(tab);
		tabForm.setVisible(false);
	}

	private boolean isUsernameValid(String username) {
		return Objects.equals(getValue().getUsername(), username) || !userService.existsById(username);
	}
	
	@Override
	protected void edit(User value) {
		userService.save(value);
		userDataProvider.refreshAll();
	}

}
