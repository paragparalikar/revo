package com.revo.llms.web.ui.vaadin.common;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import lombok.Getter;

@Getter
public abstract class TitledEditor extends Dialog {
	private static final long serialVersionUID = -4106360760089133217L;
	
	private final H3 title = new H3();
	private final Span iconSpan = new Span();
	private final Label errorLabel = new Label();
	private final HorizontalLayout header = new HorizontalLayout(iconSpan, title);
	private final FormLayout form = new FormLayout();
	private final VerticalLayout container = new VerticalLayout(header, errorLabel, form, createButtonBar());

	public TitledEditor() {
		header.setWidthFull();
		errorLabel.setWidthFull();
		errorLabel.getStyle().set("text-color", "#ff0000");
		title.getStyle()
			.set("font-size", "var(--lumo-font-size-xl)")
			.set("margin", "0");
		add(container);
	}
	
	public void setTitle(String value) {
		title.setText(value);
	}
	
	public void setIcon(Icon value) {
		iconSpan.add(value);
	}
	
	public void setError(String value) {
		errorLabel.setText(value);
	}
	
	protected Component createButtonBar() {
		final Button saveButton = new Button("Save", VaadinIcon.DATABASE.create(), event -> action());
		final Button cancelButton = new Button("Cancel", VaadinIcon.CLOSE.create(), event -> close());
		final HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
		buttonsLayout.setWidthFull();
		buttonsLayout.setAlignItems(Alignment.END);
		buttonsLayout.setJustifyContentMode(JustifyContentMode.END);
		return buttonsLayout;
	}
	
	protected abstract void action();
}
