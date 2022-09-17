package com.revo.llms.reason;

import java.util.List;
import java.util.Objects;

import com.revo.llms.category.Category;
import com.revo.llms.category.CategoryService;
import com.revo.llms.common.TitledFormEditor;
import com.revo.llms.util.DoubleToLongConverter;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;

public class ReasonEditor extends TitledFormEditor<Reason> {
	private static final long serialVersionUID = 5363061667786794597L;

	private final ReasonService reasonService;
	private final CategoryService categoryService;
	private final DataProvider<Reason, Void> dataProvider;
	
	public ReasonEditor(ReasonService reasonService, CategoryService categoryService, 
			DataProvider<Reason, Void> dataProvider) {
		super(VaadinIcon.EXCLAMATION_CIRCLE.create(), "Reason", Reason::new);
		this.categoryService = categoryService;
		this.reasonService = reasonService;
		this.dataProvider = dataProvider;
		createForm(getBinder(), getForm());
	}
	
	protected void createForm(Binder<Reason> binder, FormLayout layout) {
		final NumberField idField = new NumberField("Id");
		idField.setEnabled(false);
		binder.forField(idField)
			.withConverter(new DoubleToLongConverter())
			.bindReadOnly(Reason::getId);
		layout.add(idField);
		
		final List<Category> categories = categoryService.findAll();
		final ComboBox<Category> categoryComboBox = new ComboBox<>("Category", categories);
		categoryComboBox.setItemLabelGenerator(Category::getName);
		binder.forField(categoryComboBox)
			.asRequired()
			.bind(Reason::getCategory, Reason::setCategory);
		layout.add(categoryComboBox);
		
		final TextField textField = new TextField("Text");
		binder.forField(textField)
			.asRequired("Reason text is required")
			.withValidator(this::isTextValid, "Reason with this text already exists")
			.bind(Reason::getText, Reason::setText);
		layout.add(textField);
	}

	private boolean isTextValid(String text) {
		return null == text || Objects.equals(text, getValue().getText()) || 
				!reasonService.existsByTextIgnoreCase(text);
	}
	
	@Override
	protected void edit(Reason value) {
		reasonService.save(value);
		dataProvider.refreshAll();
	}

}
