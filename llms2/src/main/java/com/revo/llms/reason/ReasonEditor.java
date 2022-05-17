package com.revo.llms.reason;

import java.util.Objects;

import com.revo.llms.common.TitledFormEditor;
import com.revo.llms.util.DoubleToLongConverter;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;

public class ReasonEditor extends TitledFormEditor<Reason> {
	private static final long serialVersionUID = 5363061667786794597L;

	private final ReasonRepository repository;
	private final DataProvider<Reason, Void> dataProvider;
	
	public ReasonEditor(ReasonRepository repository, DataProvider<Reason, Void> dataProvider) {
		super(VaadinIcon.EXCLAMATION_CIRCLE.create(), "Reason", Reason::new);
		this.repository = repository;
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
		
		final TextField textField = new TextField("Text");
		binder.forField(textField)
			.asRequired("Reason text is required")
			.withValidator(this::isTextValid, "Reason with this text already exists")
			.bind(Reason::getText, Reason::setText);
		layout.add(textField);
	}

	private boolean isTextValid(String text) {
		return null == text || Objects.equals(text, getValue().getText()) || 
				!repository.existsByTextIgnoreCase(text);
	}
	
	@Override
	protected void edit(Reason value) {
		repository.save(value);
		dataProvider.refreshAll();
	}

}
