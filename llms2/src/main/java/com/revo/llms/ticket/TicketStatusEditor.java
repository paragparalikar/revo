package com.revo.llms.ticket;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.revo.llms.common.TitledEditor;
import com.revo.llms.part.Part;
import com.revo.llms.part.PartService;
import com.revo.llms.product.Product;
import com.revo.llms.reason.Reason;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
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
	private final ComboBox<Part> partComboBox;
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
		
		this.partComboBox = createPartComboBox();
		final Component reasonComponent = createReasonComponent();
		final Component productComponent = createProdcutComponent();
		getForm().add(productComponent, partComboBox, reasonComponent);
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
	
	private Component createProdcutComponent() {
		final ComboBox<Product> productBox = new ComboBox<>("Product");
		productBox.setItems(productFilteringDataProvider);
		productBox.setItemLabelGenerator(Product::getName);
		binder.forField(productBox).asRequired().bind(ticket -> Optional.ofNullable(ticket)
				.map(Ticket::getPart)
				.map(Part::getProduct)
				.orElse(null), (ticket, product) -> {});
		productBox.addValueChangeListener(event -> Optional.ofNullable(event.getValue()).ifPresent(product -> setParts(product)));
		return productBox;
	}
	
	private void setParts(Product product) {
		final List<Part> parts = partService.findByProductId(product.getId());
		parts.sort(Comparator.comparing(Part::getName));
		partComboBox.setItems(parts);
	}
	
	public void open(@NonNull Ticket ticket) {
		this.ticket = ticket;
		Optional.ofNullable(ticket.getPart()).map(Part::getProduct).ifPresent(this::setParts);
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
			ticketDataProvider.refreshAll();
			close();
		} catch (ValidationException e) {
			setError(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}
