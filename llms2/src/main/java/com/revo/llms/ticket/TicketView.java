package com.revo.llms.ticket;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import javax.annotation.security.PermitAll;

import org.vaadin.klaudeta.PaginatedGrid;

import com.revo.llms.LlmsConstants;
import com.revo.llms.common.Broadcaster;
import com.revo.llms.common.Broadcaster.Registration;
import com.revo.llms.common.MainLayout;
import com.revo.llms.common.TitledView;
import com.revo.llms.common.security.SecurityService;
import com.revo.llms.part.PartService;
import com.revo.llms.product.ProductFilteringDataProvider;
import com.revo.llms.product.ProductService;
import com.revo.llms.reason.ReasonFilteringDataProvider;
import com.revo.llms.reason.ReasonService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamResource;

import de.codecamp.vaadin.security.spring.access.SecuredAccess;

@PermitAll
@PageTitle("Tickets")
@SecuredAccess("hasAuthority('page-tickets')")
@RouteAlias(value = "null", layout = MainLayout.class)
@Route(value = LlmsConstants.ROUTE_TICKETS, layout = MainLayout.class)
public class TicketView extends TitledView {
	private static final long serialVersionUID = 821057894670434504L;

	private Registration broadcasterRegistration;
	private final TicketStatusEditor ticketStatusEditor;
	private final DataProvider<Ticket, Void> ticketDataProvider;
	private final PaginatedGrid<Ticket> grid = new PaginatedGrid<>();
	private final DateFormat dateFormat = new SimpleDateFormat("dd-MMM HH:mm");
	private final Anchor downloadAnchor = new Anchor();
	private final Button downloadButton = new Button("Download", VaadinIcon.DOWNLOAD.create(), event -> download());
	private final DatePicker toPicker = new DatePicker("To", LocalDate.now());
	private final DatePicker fromPicker = new DatePicker("From", LocalDate.now().minusMonths(1));
	private final HorizontalLayout dateTimePickerRow = new HorizontalLayout(fromPicker, toPicker, downloadAnchor);
			
	public TicketView(
			PartService partService,
			TicketService ticketService,
			ReasonService reasonService,
			ProductService productService,
			SecurityService securityService) {
		super(VaadinIcon.TICKET.create(), "Tickets");
		dateTimePickerRow.setWidthFull();
		dateTimePickerRow.setJustifyContentMode(JustifyContentMode.CENTER);
		dateTimePickerRow.setVerticalComponentAlignment(Alignment.END, downloadAnchor);
		this.ticketDataProvider = new TicketDataProvider(ticketService, securityService, 
				fromPicker::getValue, toPicker::getValue);
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
		addRight(dateTimePickerRow);
		toPicker.addValueChangeListener(event -> ticketDataProvider.refreshAll());
		fromPicker.addValueChangeListener(event -> ticketDataProvider.refreshAll());
		createDownloadAnchor(ticketService, securityService);
	}
	
	private void createDownloadAnchor(TicketService ticketService, SecurityService securityService) {
		final StreamResource streamResource = new StreamResource("tickets.xlsx", TicketExcelInpustStreamFactory.builder()
				.fromDateProvider(fromPicker::getValue)
				.toDateProvider(toPicker::getValue)
				.securityService(securityService)
				.ticketService(ticketService)
				.build());
		streamResource.setCacheTime(1000);
		streamResource.setHeader("Cache-Control", "private,no-cache,no-store");
		streamResource.setHeader("Content-Disposition", "attachment;filename=tickets.xls");
		streamResource.setContentType("application/vnd.ms-excel");
		downloadAnchor.setHref(streamResource);
		downloadAnchor.add(downloadButton);
	}
	
	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		final UI ui = attachEvent.getUI();
        broadcasterRegistration = Broadcaster.register(newMessage -> 
        	ui.access(() -> ticketDataProvider.refreshAll()));
	}
	
	@Override
    protected void onDetach(DetachEvent detachEvent) {
        broadcasterRegistration.remove();
        broadcasterRegistration = null;
    }

	private void createColumns(Grid<Ticket> grid) {
		grid.addColumn(Ticket::getId, "id").setHeader("Id").setWidth("4em");
		grid.addColumn(Ticket::getStatus, "status").setHeader("Status").setWidth("5em");
		grid.addColumn(ticket -> ticket.getStation().getName(), "station").setHeader("Station").setWidth("5em");
		grid.addColumn(ticket -> ticket.getDepartment().getName(), "department.name").setHeader("Department");
		grid.addColumn(ticket -> dateFormat.format(ticket.getOpenTimestamp()), "openTimestamp").setHeader("Open").setAutoWidth(true);
		grid.addColumn(ticket -> null == ticket.getClosedTimestamp() ? null : 
			dateFormat.format(ticket.getClosedTimestamp()), "closedTimestamp").setHeader("Closed").setAutoWidth(true);
		grid.addColumn(ticket -> null == ticket.getReason() ? null : ticket.getReason().getText(), "reason.text").setHeader("Reason");
		grid.addColumn(ticket -> null == ticket.getPart() ? null : ticket.getPart().getProduct().getName(), "part.product.name").setHeader("Product");
		grid.addColumn(ticket -> null == ticket.getPart() ? null : ticket.getPart().getName(), "part.name").setHeader("Part");
		grid.addColumn(new ComponentRenderer<>(this::createActionColumn));
	}

	private void download() {
		getUI().get().getPage().open(null);
	}
	
	private Component createActionColumn(Ticket ticket) {
		final Button button = new Button("Edit", VaadinIcon.EDIT.create());
		button.addClickListener(event -> ticketStatusEditor.open(ticket));
		return button;
	}
	
}
