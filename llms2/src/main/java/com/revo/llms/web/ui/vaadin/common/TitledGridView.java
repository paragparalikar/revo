package com.revo.llms.web.ui.vaadin.common;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import lombok.Getter;

@Getter
public abstract class TitledGridView<T> extends TitledView {
	private static final long serialVersionUID = 7661138175543626984L;

	private final Grid<T> grid = new Grid<>();
	
	public TitledGridView(Icon icon, String title) {
		super(icon, title);
		createColumns(grid);
		add(grid);
	}
	
	protected void createColumns(Grid<T> grid) {
		grid.addComponentColumn(this::createActionCell)
			.setSortable(false)
			.setHeader(createActionColumnHeader())
			.setTextAlign(ColumnTextAlign.END)
			.setAutoWidth(true);
	}
	
	protected Component createActionColumnHeader() {
		final Button button = new Button("New", VaadinIcon.PLUS.create());
		button.addClickListener(event -> create());
		return button;
	}
	
	protected HorizontalLayout createActionCell(T value) {
		final Button editButton = new Button("Edit", VaadinIcon.EDIT.create(), event -> edit(value));
		final Button deleteButton = new Button("Delete", VaadinIcon.TRASH.create(), event -> delete(value));
		final HorizontalLayout container = new HorizontalLayout(editButton, deleteButton);
		container.setWidthFull();
		container.setJustifyContentMode(JustifyContentMode.END);
		return container;
	}
	
	protected abstract void create();
	protected abstract void edit(T value);
	protected abstract void delete(T value);
	
}
