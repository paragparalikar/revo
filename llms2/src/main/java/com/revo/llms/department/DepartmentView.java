package com.revo.llms.department;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;

import com.revo.llms.LlmsConstants;
import com.revo.llms.common.JpaDataProvider;
import com.revo.llms.common.MainLayout;
import com.revo.llms.common.TitledGridView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PermitAll
@PageTitle("Departments")
@Route(value = LlmsConstants.ROUTE_DEPARTMENTS, layout = MainLayout.class)
public class DepartmentView extends TitledGridView<Department> {
	private static final long serialVersionUID = -4243133746000404388L;
	
	private final DepartmentEditor editor;
	private final DepartmentRepository repository;
	private final DataProvider<Department, Void> dataProvider;
	
	public DepartmentView(@Autowired DepartmentRepository repository) {
		super(VaadinIcon.BUILDING.create(), "Departments");
		this.repository = repository;
		this.dataProvider = new JpaDataProvider<>(repository);
		this.editor = new DepartmentEditor(repository, dataProvider);
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
			repository.delete(value);
			dataProvider.refreshAll();
		}catch(Exception e) {
			// TODO setError
			e.printStackTrace();
		}
	}
	
}