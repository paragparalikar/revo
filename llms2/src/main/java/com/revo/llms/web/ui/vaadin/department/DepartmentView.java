package com.revo.llms.web.ui.vaadin.department;

import org.springframework.beans.factory.annotation.Autowired;

import com.revo.llms.department.Department;
import com.revo.llms.department.DepartmentRepository;
import com.revo.llms.web.ui.vaadin.common.HasNameView;
import com.revo.llms.web.ui.vaadin.common.JpaDataProvider;
import com.revo.llms.web.ui.vaadin.common.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Departments")
@Route(value = "departments", layout = MainLayout.class)
public class DepartmentView extends HasNameView<Department> {
	private static final long serialVersionUID = -4243133746000404388L;
	
	public DepartmentView(@Autowired DepartmentRepository repository) {
		super(VaadinIcon.BUILDING.create(), "Departments", repository, 
				new DepartmentEditor(repository, new JpaDataProvider<>(repository)));
	}
	
	@Override
	protected void createColumns(Grid<Department> grid) {
		grid.addColumn(Department::getId).setHeader("Id");
		super.createColumns(grid);
	}
	
}