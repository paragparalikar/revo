package com.revo.llms.ticket;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.revo.llms.common.TitledEditor;
import com.revo.llms.part.Part;
import com.revo.llms.part.PartService;
import com.revo.llms.product.Product;
import com.revo.llms.reason.Reason;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;

import lombok.Builder;
import lombok.NonNull;

public class TicketStatusEditor extends TitledEditor {
	private static final long serialVersionUID = -3972258605300752037L;

	private Ticket ticket;
	private final PartService partService;
	private final TicketService ticketService;
	private final Binder<Ticket> binder = new Binder<>();
	private final DataProvider<Ticket, Void> ticketDataProvider;
	private final DataProvider<Reason, String> reasonFilteringDataProvider;
	private final DataProvider<Product, String> productFilteringDataProvider;
	
	@Builder
	public TicketStatusEditor(
			PartService partService,
			TicketService ticketService, 
			DataProvider<Ticket, Void> ticketDataProvider,
			DataProvider<Reason, String> reasonFilteringDataProvider,
			DataProvider<Product, String> productFilteringDataProvider) {
		this.partService = partService;
		this.ticketService = ticketService;
		this.ticketDataProvider = ticketDataProvider;
		this.reasonFilteringDataProvider = reasonFilteringDataProvider;
		this.productFilteringDataProvider = productFilteringDataProvider;
		
		setIcon(VaadinIcon.TICKET.create());
		setTitle("Close Ticket");
		createForm(getForm());
	}
	
	private void createForm(FormLayout form) {
		final Component reasonComponent = createReasonComponent();
		final ComboBox<Part> partComboBox = createPartComboBox();
		final Component productComponent = createProdcutComponent(partComboBox::setItems);
		form.add(reasonComponent, partComboBox, productComponent);
	}
	
	private Component createReasonComponent() {
		final ComboBox<Reason> reasonBox = new ComboBox<>("Reason");
		reasonBox.setItems(reasonFilteringDataProvider);
		reasonBox.setItemLabelGenerator(Reason::getText);
		binder.forField(reasonBox).asRequired().bind(Ticket::getReason, Ticket::setReason);
		return reasonBox;
	}
	
	private ComboBox<Part> createPartComboBox() {
		final ComboBox<Part> partBox = new ComboBox<>("Part");
		partBox.setItemLabelGenerator(Part::getName);
		binder.forField(partBox).asRequired()
			.bind(Ticket::getPart, Ticket::setPart);
		return partBox;
	}
	
	private Component createProdcutComponent(Consumer<List<Part>> partsConsumer) {
		final ComboBox<Product> productBox = new ComboBox<>("Product");
		productBox.setItems(productFilteringDataProvider);
		productBox.setItemLabelGenerator(Product::getName);
		productBox.addValueChangeListener(event -> {
			Optional.ofNullable(event.getValue()).ifPresent(product -> {
				final List<Part> parts = partService.findByProductId(product.getId());
				parts.sort(Comparator.comparing(Part::getName));
				partsConsumer.accept(parts);
			});
		});
		return productBox;
	}
	
	public void open(@NonNull Ticket ticket) {
		this.ticket = ticket;
		binder.readBean(ticket);
		open();
	}

	@Override
	protected void action() {
		try {
			binder.writeBean(ticket);
			ticket.setClosedTimestamp(new Date());
			ticket.setStatus(TicketStatus.CLOSED);
			ticketService.save(ticket);
			ticketDataProvider.refreshItem(ticket);
			close();
		} catch (ValidationException e) {
			setError(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}
