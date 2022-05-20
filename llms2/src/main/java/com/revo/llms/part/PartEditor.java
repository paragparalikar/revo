package com.revo.llms.part;

import java.util.Objects;

import com.revo.llms.common.TitledFormEditor;
import com.revo.llms.product.Product;
import com.revo.llms.util.DoubleToLongConverter;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class PartEditor extends TitledFormEditor<Part> {
	private static final long serialVersionUID = -2419791616710226909L;

	private Product product;
	private final PartService partService;
	private final PartDataProvider dataProvider;
	
	public PartEditor(
			PartService partService,
			PartDataProvider dataProvider2) {
		super(VaadinIcon.COGS.create(), "Parts", Part::new);
		this.partService = partService;
		this.dataProvider = dataProvider2;
		createForm(getBinder(), getForm());
	}
	
	private void createForm(Binder<Part> binder, FormLayout layout) {
		final NumberField idField = new NumberField("Id");
		idField.setEnabled(false);
		binder.forField(idField)
			.withConverter(new DoubleToLongConverter())
			.bindReadOnly(Part::getId);
		layout.add(idField);
		
		final TextField nameField = new TextField("Name");
		binder.forField(nameField)
			.asRequired("Part name is required")
			.withValidator(this::isNameValid, "Part with same name already exists")
			.bind(Part::getName, Part::setName);
		layout.add(nameField);
	}
	
	private boolean isNameValid(String name) {
		return null == name || Objects.equals(name, getValue().getName()) 
				|| !partService.existsByNameAndProductId(name, product.getId());
	}

	public void open(Product product, Part part) {
		this.product = product;
		open(part);
	}

	@Override
	protected void edit(Part part) {
		part.setProduct(product);
		partService.save(part);
		dataProvider.refreshAll();
	}

}
