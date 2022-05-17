package com.revo.llms.web.ui.vaadin.common;

import java.util.function.Supplier;

import org.springframework.data.repository.CrudRepository;

import com.revo.llms.common.HasName;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;

import lombok.Getter;

@Getter
public class HasNameEditor<T extends HasName, F> extends Dialog {
	private static final long serialVersionUID = -1522582488939995736L;

	private T value;
	private final Binder<T> binder;
	private final String entityName;
	private final H3 header = new H3();
	private final Supplier<T> modelSupplier;
	private final DataProvider<T, F> dataProvider;
	private final CrudRepository<T, Long> repository;

	public HasNameEditor(
			Binder<T> binder,
			String entityName,
			Supplier<T> modelSupplier,
			DataProvider<T, F> dataProvider, 
			CrudRepository<T, Long> repository) {
		this.binder = binder;
		this.entityName = entityName;
		this.repository = repository;
		this.dataProvider = dataProvider;
		this.modelSupplier = modelSupplier;
		addThemeVariants(DialogVariant.LUMO_NO_PADDING);
		add(new VerticalLayout(header, createForm(binder, new FormLayout()), createButtonBar()));
	}
	
	protected Component createForm(Binder<T> binder, FormLayout layout) {
		final TextField nameField = new TextField("Name");
		binder.forField(nameField)
			.asRequired("Name is required")
			.bind(T::getName, T::setName);
		layout.add(nameField);
		return layout;
	}
	
	protected Component createButtonBar() {
		final Button saveButton = new Button("Save", VaadinIcon.DATABASE.create(), this::save);
		final Button cancelButton = new Button("Cancel", VaadinIcon.CLOSE.create(), event -> close());
		final HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
		buttonsLayout.setWidthFull();
		buttonsLayout.setAlignItems(Alignment.END);
		buttonsLayout.setJustifyContentMode(JustifyContentMode.END);
		return buttonsLayout;
	}
	
	private void save(ClickEvent<Button> event) {
		try {
			binder.writeBean(value);
			save(value);
			dataProvider.refreshAll();
			close();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}
	
	protected void save(T value) {
		repository.save(value);
	}
	
	public void open(T value) {
		setValue(value);
		open();
	}
	
	public void setValue(T value) {
		binder.readBean(value);
		if(null == value) {
			this.value = modelSupplier.get();
			header.setText("Create New " + entityName);
		} else {
			this.value = value;
			header.setText("Edit - " + value.getName());
		}
	}
	
}
