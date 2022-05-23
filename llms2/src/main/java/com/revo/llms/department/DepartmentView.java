package com.revo.llms.department;

import javax.annotation.security.PermitAll;

import com.revo.llms.LlmsConstants;
import com.revo.llms.common.MainLayout;
import com.revo.llms.common.TitledGridView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.codecamp.vaadin.security.spring.access.SecuredAccess;

@PermitAll
@PageTitle("Departments")
@SecuredAccess("hasAuthority('page-departments')")
@Route(value = LlmsConstants.ROUTE_DEPARTMENTS, layout = MainLayout.class)
public class DepartmentView extends TitledGridView<Department> {
	private static final long serialVersionUID = -4243133746000404388L;
	
	private final DepartmentEditor editor;
	private final DepartmentService departmentService;
	private final DataProvider<Department, Void> dataProvider;
	
	public DepartmentView(DepartmentService departmentService) {
		super(VaadinIcon.BUILDING.create(), "Departments");
		this.departmentService = departmentService;
		this.dataProvider = new DepartmentDataProvider(departmentService);
		this.editor = new DepartmentEditor(departmentService, dataProvider);
		final Grid<Department> grid = new Grid<>();
		grid.setItems(dataProvider);
		createColumns(grid);
		add(grid, editor);
	}
	
	@Override
	protected void createColumns(Grid<Department> grid) {
		grid.addColumn(Department::getId, "id").setHeader("Id");
		grid.addColumn(Department::getCode, "code").setHeader("Code");
		grid.addColumn(Department::getName, "name").setHeader("Name");
		super.createColumns(grid);
	}

	@Override
	protected void create() {
		editor.open(null);
	}

	@Override
	protected void edit(Department value) {
		editor.open(value);
	}

	@Override
	protected void delete(Department value) {
		try {
			departmentService.delete(value);
			dataProvider.refreshAll();
		}catch(Exception e) {
			setError(e.getMessage());
			e.printStackTrace();
		}
	}
	
}