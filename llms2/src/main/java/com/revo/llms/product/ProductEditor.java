package com.revo.llms.product;

import java.util.Objects;

import com.revo.llms.common.TitledFormEditor;
import com.revo.llms.util.DoubleToLongConverter;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;

public class ProductEditor extends TitledFormEditor<Product> {
	private static final long serialVersionUID = -6947933593729998570L;

	private final ProductService productService;
	private final DataProvider<Product, Void> dataProvider;
	
	public ProductEditor(ProductService productService, DataProvider<Product, Void> dataProvider) {
		super(VaadinIcon.CART.create(), "Product", Product::new);
		this.productService = productService;
		this.dataProvider = dataProvider;
		createForm(getBinder(), getForm());
	}
	
	protected void createForm(Binder<Product> binder, FormLayout layout) {
		final NumberField idField = new NumberField("Id");
		idField.setEnabled(false);
		binder.forField(idField)
			.withConverter(new DoubleToLongConverter())
			.bindReadOnly(Product::getId);
		layout.add(idField);
		
		final TextField nameField = new TextField("Name");
		binder.forField(nameField)
			.asRequired("Product name is required")
			.withValidator(this::isNameValid, "Product with this name already exists")
			.bind(Product::getName, Product::setName);
		layout.add(nameField);
	}
	
	private boolean isNameValid(String name) {
		return null == name || Objects.equals(name, getValue().getName()) || 
				!productService.existsByNameIgnoreCase(name);
	}

	@Override
	protected void edit(Product value) {
		productService.save(value);
		dataProvider.refreshAll();
	}

}
