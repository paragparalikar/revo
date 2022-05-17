package com.revo.llms.web.ui.vaadin.department;

import org.springframework.beans.factory.annotation.Autowired;

import com.revo.llms.department.Department;
import com.revo.llms.department.DepartmentRepository;
import com.revo.llms.web.ui.vaadin.common.JpaDataProvider;
import com.revo.llms.web.ui.vaadin.common.MainLayout;
import com.revo.llms.web.ui.vaadin.common.TitledGridView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Departments")
@Route(value = "departments", layout = MainLayout.class)
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
		getGrid().setItems(dataProvider);
		add(editor);
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