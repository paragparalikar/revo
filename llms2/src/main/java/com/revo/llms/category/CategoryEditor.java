package com.revo.llms.category;

import java.util.Optional;

import com.revo.llms.common.TitledFormEditor;
import com.revo.llms.util.DoubleToLongConverter;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;

public class CategoryEditor extends TitledFormEditor<Category> {
	private static final long serialVersionUID = 1L;
	
	private final CategoryService categoryService;
	private final DataProvider<Category, Void> dataProvider;

	public CategoryEditor(CategoryService categoryService, DataProvider<Category, Void> dataProvider) {
		super(VaadinIcon.SPLIT.create(), "Reason Category", Category::new);
		this.categoryService = categoryService;
		this.dataProvider = dataProvider;
		createForm(getBinder(), getForm());
	}

	protected void createForm(Binder<Category> binder, FormLayout layout) {
		final NumberField idField = new NumberField("Id");
		idField.setEnabled(false);
		binder.forField(idField)
			.withConverter(new DoubleToLongConverter())
			.bindReadOnly(Category::getId);
		layout.add(idField);
		
		final TextField nameField = new TextField("Name");
		binder.forField(nameField)
			.asRequired("Category text is required")
			.withValidator(this::isTextValid, "Category with this name already exists")
			.bind(Category::getName, Category::setName);
		layout.add(nameField);
	}

	private boolean isTextValid(String name) {
		if(null == name) return false;
		if(0 == name.trim().length()) return false;
		final Long id = Optional.ofNullable(getValue()).map(Category::getId).orElse(0L);
		return !categoryService.existsByNameIgnoreCaseAndIdNot(name, id);
	}
	
	@Override
	protected void edit(Category value) {
		categoryService.save(value);
		dataProvider.refreshAll();
	}

}
