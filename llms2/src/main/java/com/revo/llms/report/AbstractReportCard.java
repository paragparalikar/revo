package com.revo.llms.report;

import java.util.List;

import com.github.appreciated.card.Card;
import com.revo.llms.ticket.Ticket;

public abstract class AbstractReportCard extends Card {
	private static final long serialVersionUID = -6088686154462843764L;

	public AbstractReportCard() {
		setBorderRadius("5px");
		setBackground("#213345");
		setMinWidth("250px");
		setMinHeight("250px");
	}
	
	public abstract void update(List<Ticket> tickets);
}
