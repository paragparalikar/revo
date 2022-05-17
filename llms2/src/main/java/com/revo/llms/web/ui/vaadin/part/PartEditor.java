package com.revo.llms.web.ui.vaadin.part;

import org.springframework.data.repository.CrudRepository;

import com.revo.llms.part.Part;
import com.revo.llms.product.Product;
import com.revo.llms.web.ui.vaadin.common.HasNameEditor;
import com.revo.llms.web.ui.vaadin.util.DoubleToLongConverter;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;

public class PartEditor extends HasNameEditor<Part, Void> {
	private static final long serialVersionUID = -2419791616710226909L;

	private Product product;
	private FormLayout layout;
	private Binder<Part> binder;
	
	public PartEditor(
			DataProvider<Part, Void> partDataProvider, 
			CrudRepository<Part, Long> repository) {
		super(new Binder<>(Part.class), "Part", Part::new, partDataProvider, repository);
		createForm();
	}
	
	private void createForm() {
		final NumberField idField = new NumberField("Id");
		idField.setEnabled(false);
		binder.forField(idField)
			.withConverter(new DoubleToLongConverter())
			.bindReadOnly(Part::getId);
		layout.add(idField);
		
		final TextField nameField = new TextField("Name");
		binder.forField(nameField)
			.asRequired("Name is required")
			.bind(Part::getName, Part::setName);
		layout.add(nameField);
	}

	public void open(Product product, Part part) {
		this.product = product;
		open(part);
	}
	
	@Override
	protected void save(ClickEvent<Button> event) {
		getValue().setProduct(product);
		super.save(event);
	}
	
	@Override
	protected Component createForm(Binder<Part> binder, FormLayout layout) {
		this.binder = binder;
		this.layout = layout;
		return layout;
	}
}
