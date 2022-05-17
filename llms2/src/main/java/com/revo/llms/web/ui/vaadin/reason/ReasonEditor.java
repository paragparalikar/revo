package com.revo.llms.web.ui.vaadin.reason;

import org.springframework.data.repository.CrudRepository;

import com.revo.llms.reason.Reason;
import com.revo.llms.web.ui.vaadin.common.HasNameEditor;
import com.revo.llms.web.ui.vaadin.util.DoubleToLongConverter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;

public class ReasonEditor extends HasNameEditor<Reason, Void> {
	private static final long serialVersionUID = 5363061667786794597L;

	public ReasonEditor(DataProvider<Reason, Void> dataProvider, CrudRepository<Reason, Long> repository) {
		super(new Binder<>(Reason.class), "Reason", Reason::new, dataProvider, repository);
	}
	
	@Override
	protected Component createForm(Binder<Reason> binder, FormLayout layout) {
		final NumberField idField = new NumberField("Id");
		idField.setEnabled(false);
		binder.forField(idField)
			.withConverter(new DoubleToLongConverter())
			.bindReadOnly(Reason::getId);
		layout.add(idField);
		return super.createForm(binder, layout);
	}

}
