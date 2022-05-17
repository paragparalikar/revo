package com.revo.llms.department;

import java.util.Objects;

import com.revo.llms.common.TitledFormEditor;
import com.revo.llms.util.DoubleToIntegerConverter;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;

public class DepartmentEditor extends TitledFormEditor<Department> {
	private static final long serialVersionUID = -7011701870382258164L;
	
	private final DepartmentRepository repository;
	private final DataProvider<Department, Void> dataProvider;
	
	public DepartmentEditor(
			DepartmentRepository repository, 
			DataProvider<Department, Void> dataProvider) {
		super(VaadinIcon.BUILDING.create(), "Department", Department::new);
		this.repository = repository;
		this.dataProvider = dataProvider;
		createForm(getBinder(), getForm());
	}
	
	protected void createForm(Binder<Department> binder, FormLayout layout) {
		final NumberField idField = new NumberField("Id");
		idField.setEnabled(false);
		binder.forField(idField)
			.withConverter(new DoubleToIntegerConverter())
			.bindReadOnly(Department::getId);
		layout.add(idField);
		
		final NumberField codeField = new NumberField("Code");
		codeField.setStep(1);
		binder.forField(codeField)
			.asRequired("Department code is required")
			.withConverter(new DoubleToIntegerConverter())
			.withValidator(this::isCodeValid, "Department with this code already exists")
			.bind(Department::getCode, Department::setCode);
		layout.add(codeField);
		
		final TextField nameField = new TextField("Name");
		binder.forField(nameField)
			.asRequired("Department name is required")
			.withValidator(this::isNameValid, "Department with this name already exists")
			.bind(Department::getName, Department::setName);
		layout.add(nameField);
	}
	
	private boolean isCodeValid(Integer code) {
		return null == code || Objects.equals(code, getValue().getCode()) || !repository.existsByCode(code);
	}
	
	private boolean isNameValid(String name) {
		return null == name || Objects.equals(name, getValue().getName()) || !repository.existsByNameIgnoreCase(name);
	}
	
	@Override
	protected void edit(Department value) {
		repository.save(value);
		dataProvider.refreshAll();
	}
	
}
