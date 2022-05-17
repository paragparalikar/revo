package com.revo.llms.web.ui.vaadin.product;

import org.springframework.data.repository.CrudRepository;

import com.revo.llms.product.Product;
import com.revo.llms.web.ui.vaadin.common.HasNameEditor;
import com.revo.llms.web.ui.vaadin.util.DoubleToLongConverter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;

public class ProductEditor extends HasNameEditor<Product, Void> {
	private static final long serialVersionUID = -6947933593729998570L;

	public ProductEditor(DataProvider<Product, Void> dataProvider, CrudRepository<Product, Long> repository) {
		super(new Binder<>(Product.class), "Product", Product::new, dataProvider, repository);
	}
	
	@Override
	protected Component createForm(Binder<Product> binder, FormLayout layout) {
		final NumberField idField = new NumberField("Id");
		idField.setEnabled(false);
		binder.forField(idField)
			.withConverter(new DoubleToLongConverter())
			.bindReadOnly(Product::getId);
		layout.add(idField);
		return super.createForm(binder, layout);
	}

}
