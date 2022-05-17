package com.revo.llms.web.ui.vaadin.department;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revo.llms.department.Department;
import com.revo.llms.web.ui.vaadin.common.HasNameEditor;
import com.revo.llms.web.ui.vaadin.util.DoubleToIntegerConverter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;

public class DepartmentEditor extends HasNameEditor<Department, Void> {
	private static final long serialVersionUID = -7011701870382258164L;
	
	public DepartmentEditor(
			JpaRepository<Department, Long> repository, 
			DataProvider<Department, Void> dataProvider) {
		super(new Binder<>(Department.class), "Department", Department::new, dataProvider, repository);
	}
	
	@Override
	protected Component createForm(Binder<Department> binder, FormLayout layout) {
		final NumberField codeField = new NumberField("Id");
		codeField.setStep(1);
		binder.forField(codeField)
			.withConverter(new DoubleToIntegerConverter())
			.asRequired("Department id is required")
			.bind(Department::getId, Department::setId);
		layout.add(codeField);
		return super.createForm(binder, layout);
	}
	
}
