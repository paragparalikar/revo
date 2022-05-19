package com.revo.llms.dashboard;

import com.github.appreciated.card.Card;

public abstract class AbstractCard extends Card {
	private static final long serialVersionUID = -6088686154462843764L;

	public AbstractCard() {
		setBorderRadius("5px");
		setBackground("#213345");
		setMinWidth("250px");
		setMinHeight("250px");
	}
	
	public abstract void update();
	
}
