package com.revo.oms.ui;

import com.vaadin.annotations.Push;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

@Push
@SpringUI
@SpringViewDisplay
public class MainUI extends UI {
	private static final long serialVersionUID = -9063255085666902865L;
	
	private Panel root;

	@Override
	protected void init(VaadinRequest request) {
        root = new Panel();
        root.setSizeFull();
        setContent(root);
	}

}
