package com.revo.llms.web.ui.vaadin.common;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revo.llms.common.HasName;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import lombok.Getter;

@Getter
public class HasNameView<T extends HasName> extends VerticalLayout {
	private static final long serialVersionUID = -6215249907986160768L;

	private final Grid<T> grid;
	private final H2 title = new H2();
	private final HasNameEditor<T, ?> editor;
	private final JpaRepository<T, Long> repository;
	
	@SuppressWarnings("deprecation")
	public HasNameView(String title, JpaRepository<T, Long> repository, HasNameEditor<T, ?> editor) {
		this.editor = editor;
		this.repository = repository;
		this.grid = new Grid<T>();
		createColumns(grid);
		grid.addComponentColumn(this::createActionCell)
			.setSortable(false)
			.setHeader(createCreateButton())
			.setTextAlign(ColumnTextAlign.END)
			.setAutoWidth(true);
		this.title.setText(title);
		add(this.title, grid, editor);
		grid.setDataProvider(editor.getDataProvider());
	}
	
	protected void createColumns(Grid<T> grid) {
		grid.addColumn(T::getName, "name").setHeader("Name");
	}
	
	private Component createCreateButton() {
		final Button button = new Button("New", VaadinIcon.PLUS.create());
		button.addClickListener(event -> editor.open(null));
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
	
	protected void edit(T value) {
		editor.open(value);
	}
	
	protected void delete(T department) {
		repository.delete(department);
		editor.getDataProvider().refreshAll();
	}
}
