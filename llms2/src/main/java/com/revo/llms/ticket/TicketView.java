package com.revo.llms.ticket;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.klaudeta.PaginatedGrid;

import com.revo.llms.LlmsConstants;
import com.revo.llms.common.MainLayout;
import com.revo.llms.common.TitledView;
import com.revo.llms.common.security.SecurityService;
import com.revo.llms.part.PartRepository;
import com.revo.llms.product.ProductRepository;
import com.revo.llms.reason.ReasonRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PermitAll
@PageTitle("Tickets")
@Route(value = LlmsConstants.ROUTE_TICKETS, layout = MainLayout.class)
public class TicketView extends TitledView {
	private static final long serialVersionUID = 821057894670434504L;

	private final TicketStatusEditor editor;
	private final DataProvider<Ticket, Void> dataProvider;
	private final PaginatedGrid<Ticket> grid = new PaginatedGrid<>();
	private final DateFormat dateFormat = new SimpleDateFormat("dd-MMM HH:mm");
			
	public TicketView(
			@Autowired SecurityService securityService,
			@Autowired TicketRepository repository,
			@Autowired PartRepository partRepository,
			@Autowired ReasonRepository reasonRepository,
			@Autowired ProductRepository productRepository) {
		super(VaadinIcon.TICKET.create(), "Tickets");
		this.dataProvider = new TicketDataProvider(repository, securityService);
		this.editor = new TicketStatusEditor(repository, partRepository, reasonRepository, productRepository, dataProvider);
		grid.setItems(dataProvider);
		grid.setPageSize(10);
		grid.setPaginatorSize(5);
		createColumns(grid);
		add(grid, editor);
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
		grid.addColumn(new ComponentRenderer<>(this::createActionColumn));
	}
	
	private Component createActionColumn(Ticket ticket) {
		final Button button = new Button("Close", VaadinIcon.CHECK.create());
		button.setEnabled(!TicketStatus.CLOSED.equals(ticket.getStatus()));
		button.addClickListener(event -> editor.open(ticket));
		return button;
	}
	
}
