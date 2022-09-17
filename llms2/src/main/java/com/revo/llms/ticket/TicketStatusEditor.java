package com.revo.llms.ticket;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.revo.llms.category.Category;
import com.revo.llms.common.TitledEditor;
import com.revo.llms.part.Part;
import com.revo.llms.part.PartService;
import com.revo.llms.product.Product;
import com.revo.llms.reason.Reason;
import com.revo.llms.reason.ReasonService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
	private final ReasonService reasonService;
	private final Binder<Ticket> binder = new Binder<>();
	
	private final ComboBox<Part> partComboBox;
	private final ComboBox<Reason> reasonComboBox;
	private final ComboBox<Product> productComboBox;
	private final ComboBox<Category> categoryComboBox;
	private final DataProvider<Ticket, Void> ticketDataProvider;
	private final DataProvider<Reason, String> reasonDataProvider;
	private final DataProvider<Product, String> productDataProvider;
	private final DataProvider<Category, String> categoryDataProvider;
	
	@Builder
	public TicketStatusEditor(
			PartService partService,
			TicketService ticketService, 
			ReasonService reasonService,
			DataProvider<Ticket, Void> ticketDataProvider,
			DataProvider<Reason, String> reasonDataProvider,
			DataProvider<Product, String> productDataProvider, 
			DataProvider<Category, String> categoryDataProvider) {
		this.partService = partService;
		this.ticketService = ticketService;
		this.reasonService = reasonService;
		
		this.ticketDataProvider = ticketDataProvider;
		this.reasonDataProvider = reasonDataProvider;
		this.productDataProvider = productDataProvider;
		this.categoryDataProvider = categoryDataProvider;
		
		setIcon(VaadinIcon.TICKET.create());
		setTitle("Close Ticket");
		
		partComboBox = createPartComponent();
		productComboBox = createProdcutComponent();
		reasonComboBox = createReasonComponent();
		categoryComboBox = createCategoryComboBox();
		
		final VerticalLayout layout = new VerticalLayout(productComboBox, partComboBox, categoryComboBox, reasonComboBox);
		layout.setMinWidth("40em");
		getForm().add(layout);
	}
	
	private ComboBox<Category> createCategoryComboBox() {
		final ComboBox<Category> categoryBox = new ComboBox<>("Reason Category");
		categoryBox.setWidthFull();
		categoryBox.setItems(categoryDataProvider);
		categoryBox.setItemLabelGenerator(Category::getName);
		binder.forField(categoryBox).asRequired().bind(ticket -> Optional.ofNullable(ticket)
				.map(Ticket::getReason)
				.map(Reason::getCategory)
				.orElse(null), (ticket, category) -> {});
		categoryBox.addValueChangeListener(event -> Optional.ofNullable(event.getValue()).ifPresent(this::setReasons));
		return categoryBox;
	}
	
	private void setReasons(Category category) {
		final List<Reason> reasons = reasonService.findByCategory(category);
		reasons.sort(Comparator.comparing(Reason::getText));
		reasonComboBox.setItems(reasons);
		reasonComboBox.setValue(Optional.ofNullable(ticket)
				.map(Ticket::getReason)
				.filter(reason -> Objects.equals(category, reason.getCategory()))
				.orElse(null));
	}
	
	private ComboBox<Product> createProdcutComponent() {
		final ComboBox<Product> productBox = new ComboBox<>("Product");
		productBox.setWidthFull();
		productBox.setItems(productDataProvider);
		productBox.setItemLabelGenerator(Product::getName);
		binder.forField(productBox).asRequired().bind(ticket -> Optional.ofNullable(ticket)
				.map(Ticket::getPart)
				.map(Part::getProduct)
				.orElse(null), (ticket, product) -> {});
		productBox.addValueChangeListener(event -> Optional.ofNullable(event.getValue()).ifPresent(this::setParts));
		return productBox;
	}
	
	private ComboBox<Part> createPartComponent() {
		final ComboBox<Part> partBox = new ComboBox<>("Part");
		partBox.setWidthFull();
		partBox.setItemLabelGenerator(Part::getName);
		binder.forField(partBox).asRequired()
			.bind(Ticket::getPart, Ticket::setPart);
		return partBox;
	}
	
	private void setParts(Product product) {
		final List<Part> parts = partService.findByProductId(product.getId());
		parts.sort(Comparator.comparing(Part::getName));
		partComboBox.setItems(parts);
		Optional.ofNullable(ticket)
			.map(Ticket::getPart)
			.filter(part -> Objects.equals(product, part.getProduct()))
			.ifPresent(partComboBox::setValue);
	}
	
	private ComboBox<Reason> createReasonComponent() {
		final ComboBox<Reason> reasonBox = new ComboBox<>("Reason");
		reasonBox.setWidthFull();
		reasonBox.setItems(reasonDataProvider);
		reasonBox.setItemLabelGenerator(Reason::getText);
		binder.forField(reasonBox).asRequired().bind(Ticket::getReason, Ticket::setReason);
		return reasonBox;
	}
	
	public void open(@NonNull Ticket ticket) {
		this.ticket = ticket;
		Optional.ofNullable(ticket.getPart()).map(Part::getProduct).ifPresent(this::setParts);
		Optional.ofNullable(ticket.getReason()).map(Reason::getCategory).ifPresent(this::setReasons);
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
