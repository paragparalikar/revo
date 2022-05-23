package com.revo.llms.ticket;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.annotation.security.PermitAll;

import org.vaadin.klaudeta.PaginatedGrid;

import com.revo.llms.LlmsConstants;
import com.revo.llms.common.MainLayout;
import com.revo.llms.common.TitledView;
import com.revo.llms.common.security.SecurityService;
import com.revo.llms.part.PartService;
import com.revo.llms.product.ProductFilteringDataProvider;
import com.revo.llms.product.ProductService;
import com.revo.llms.reason.ReasonFilteringDataProvider;
import com.revo.llms.reason.ReasonService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.codecamp.vaadin.security.spring.access.SecuredAccess;

@PermitAll
@PageTitle("Tickets")
@SecuredAccess("hasAuthority('page-tickets')")
@Route(value = LlmsConstants.ROUTE_TICKETS, layout = MainLayout.class)
public class TicketView extends TitledView {
	private static final long serialVersionUID = 821057894670434504L;

	private final TicketStatusEditor ticketStatusEditor;
	private final DataProvider<Ticket, Void> ticketDataProvider;
	private final PaginatedGrid<Ticket> grid = new PaginatedGrid<>();
	private final DateFormat dateFormat = new SimpleDateFormat("dd-MMM HH:mm");
			
	public TicketView(
			PartService partService,
			TicketService ticketService,
			ReasonService reasonService,
			ProductService productService,
			SecurityService securityService) {
		super(VaadinIcon.TICKET.create(), "Tickets");
		this.ticketDataProvider = new TicketDataProvider(ticketService, securityService);
		this.ticketStatusEditor = TicketStatusEditor.builder()
				.partService(partService)
				.ticketService(ticketService)
				.ticketDataProvider(ticketDataProvider)
				.reasonFilteringDataProvider(new ReasonFilteringDataProvider(reasonService))
				.productFilteringDataProvider(new ProductFilteringDataProvider(productService))
				.build();
		
		grid.setItems(ticketDataProvider);
		grid.setPageSize(10);
		grid.setPaginatorSize(5);
		createColumns(grid);
		add(grid, ticketStatusEditor);
	}

	private void createColumns(Grid<Ticket> grid) {
		grid.addColumn(Ticket::getId, "id").setHeader("Id").setWidth("4em");
		grid.addColumn(Ticket::getStatus, "status").setHeader("Status").setWidth("5em");
		grid.addColumn(Ticket::getStationId, "stationId").setHeader("Station").setWidth("5em");
		grid.addColumn(ticket -> ticket.getDepartment().getName(), "department.name").setHeader("Department");
		grid.addColumn(ticket -> dateFormat.format(ticket.getOpenTimestamp()), "openTimestamp").setHeader("Open").setAutoWidth(true);
		grid.addColumn(ticket -> null == ticket.getClosedTimestamp() ? null : 
			dateFormat.format(ticket.getClosedTimestamp()), "closedTimestamp").setHeader("Closed").setAutoWidth(true);
		grid.addColumn(ticket -> null == ticket.getReason() ? null : ticket.getReason().getText(), "reason.text").setHeader("Reason");
		grid.addColumn(ticket -> null == ticket.getPart() ? null : ticket.getPart().getProduct().getName(), "part.product.name").setHeader("Product");
		grid.addColumn(ticket -> null == ticket.getPart() ? null : ticket.getPart().getName(), "part.name").setHeader("Part");
		grid.addColumn(new ComponentRenderer<>(this::createActionColumn)).setHeader(createActionColumnHeader());
	}
	
	private Component createActionColumnHeader() {
		final Button button = new Button("Refresh", VaadinIcon.REFRESH.create());
		button.addClickListener(event -> ticketDataProvider.refreshAll());
		return button;
	}
	
	private Component createActionColumn(Ticket ticket) {
		final Button button = new Button("Close", VaadinIcon.CHECK.create());
		button.setEnabled(!TicketStatus.CLOSED.equals(ticket.getStatus()));
		button.addClickListener(event -> ticketStatusEditor.open(ticket));
		return button;
	}
	
}
