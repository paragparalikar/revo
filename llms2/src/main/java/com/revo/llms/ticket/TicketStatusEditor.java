package com.revo.llms.ticket;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.revo.llms.common.JpaDataProvider;
import com.revo.llms.common.TitledEditor;
import com.revo.llms.part.Part;
import com.revo.llms.part.PartRepository;
import com.revo.llms.product.Product;
import com.revo.llms.product.ProductRepository;
import com.revo.llms.reason.Reason;
import com.revo.llms.reason.ReasonRepository;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;

import lombok.NonNull;

public class TicketStatusEditor extends TitledEditor {
	private static final long serialVersionUID = -3972258605300752037L;

	private Ticket ticket;
	private final TicketRepository repository;
	private final PartRepository partRepository;
	private final ReasonRepository reasonRepository;
	private final ProductRepository productRepository;
	private final Binder<Ticket> binder = new Binder<>();
	private final DataProvider<Ticket, Void> dataProvider;
	
	public TicketStatusEditor(
			TicketRepository repository, 
			PartRepository partRepository,
			ReasonRepository reasonRepository,
			ProductRepository productRepository,
			DataProvider<Ticket, Void> dataProvider) {
		this.repository = repository;
		this.dataProvider = dataProvider;
		this.partRepository = partRepository;
		this.reasonRepository = reasonRepository;
		this.productRepository = productRepository;
		setIcon(VaadinIcon.TICKET.create());
		setTitle("Close Ticket");
		createForm(getForm());
	}
	
	private void createForm(FormLayout form) {
		final ComboBox<Reason> reasonBox = new ComboBox<>("Reason");
		reasonBox.setItems(new JpaDataProvider<>(reasonRepository));
		reasonBox.setItemLabelGenerator(Reason::getText);
		binder.forField(reasonBox).asRequired().bind(Ticket::getReason, Ticket::setReason);
		form.add(reasonBox);
		
		final ComboBox<Part> partBox = new ComboBox<>("Part");
		final ComboBox<Product> productBox = new ComboBox<>("Product");
		productBox.setItems(new JpaDataProvider<>(productRepository));
		productBox.setItemLabelGenerator(Product::getName);
		productBox.addValueChangeListener(event -> {
			Optional.ofNullable(event.getValue()).ifPresent(product -> {
				final List<Part> parts = partRepository.findByProductId(product.getId());
				parts.sort(Comparator.comparing(Part::getName));
				partBox.setItems(parts);
			});
		});
		form.add(productBox);
		
		
		partBox.setItemLabelGenerator(Part::getName);
		binder.forField(partBox).asRequired()
			.bind(Ticket::getPart, Ticket::setPart);
		form.add(partBox);
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
			repository.save(ticket);
			dataProvider.refreshItem(ticket);
			close();
		} catch (ValidationException e) {
			setError(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}
