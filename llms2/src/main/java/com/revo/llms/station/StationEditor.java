package com.revo.llms.station;

import com.revo.llms.common.TitledFormEditor;
import com.revo.llms.util.DoubleToIntegerConverter;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;

public class StationEditor extends TitledFormEditor<Station> {
	private static final long serialVersionUID = 1L;

	private final StationService stationService;
	private final DataProvider<Station, ?> dataProvider;
	
	public StationEditor(StationService stationService, DataProvider<Station, ?> dataProvider) {
		super(VaadinIcon.TABLE.create(), "Station", null);
		this.stationService = stationService;
		this.dataProvider = dataProvider;
		createForm(getBinder(), getForm());
	}
	
	private void createForm(Binder<Station> binder, FormLayout layout) {
		final NumberField idField = new NumberField("Id");
		idField.setEnabled(false);
		binder.forField(idField)
			.withConverter(new DoubleToIntegerConverter())
			.bindReadOnly(Station::getId);
		layout.add(idField);
		
		final TextField textField = new TextField("Name");
		binder.forField(textField)
			.asRequired("Station name is required")
			.withValidator(this::isTextValid, "Station with this name already exists")
			.bind(Station::getName, Station::setName);
		layout.add(textField);
	}
	
	private boolean isTextValid(String name) {
		if(null == name) return false;
		if(0 == name.trim().length()) return false;
		return !stationService.existsByNameIgnoreCaseAndIdNot(name, getValue().getId());
	}
	
	@Override
	protected void edit(Station value) {
		stationService.save(value);
		dataProvider.refreshItem(value);
	}

}
