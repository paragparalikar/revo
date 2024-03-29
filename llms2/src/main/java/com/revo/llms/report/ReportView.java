package com.revo.llms.report;

import static com.revo.llms.LlmsConstants.ROUTE_REPORTS;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;

import com.revo.llms.common.Broadcaster;
import com.revo.llms.common.Broadcaster.Registration;
import com.revo.llms.common.MainLayout;
import com.revo.llms.ticket.Ticket;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PermitAll
@PageTitle("Report")
@Route(value = ROUTE_REPORTS, layout = MainLayout.class)
public class ReportView extends VerticalLayout {
	private static final long serialVersionUID = 9218395343919664712L;

	private final ReportService reportService;
	private Registration broadcasterRegistration;
	private final TicketTimeByCategoryCard ticketTimeByCategoryCard;
	private final TicketCountByCategoryCard ticketCountByCategoryCard;
	private final TicketCountByDepartmentCard ticketCountByDepartmentCard;
	private final TicketTimeByDepartmentCard ticketTimeByDepartmentCard;
	private final DateTimePicker toPicker = new DateTimePicker("To", LocalDateTime.now());
	private final DateTimePicker fromPicker = new DateTimePicker("From", LocalDateTime.now().minusMonths(1));
	private final HorizontalLayout dateTimePickerRow = new HorizontalLayout(fromPicker, toPicker);
	private final HorizontalLayout row1 = new HorizontalLayout();
	private final HorizontalLayout row2 = new HorizontalLayout();
	
	public ReportView(ReportService reportService) {
		this.reportService = reportService;
		dateTimePickerRow.setWidthFull();
		dateTimePickerRow.setJustifyContentMode(JustifyContentMode.CENTER);
		
		this.ticketTimeByCategoryCard = new TicketTimeByCategoryCard(reportService);
		this.ticketCountByCategoryCard = new TicketCountByCategoryCard(reportService);
		this.ticketTimeByDepartmentCard = new TicketTimeByDepartmentCard(reportService);
		this.ticketCountByDepartmentCard = new TicketCountByDepartmentCard(reportService);
		
		row1.add(ticketCountByDepartmentCard, ticketTimeByDepartmentCard);
		row1.setWidthFull();
		row1.setJustifyContentMode(JustifyContentMode.EVENLY);
		
		row2.add(ticketCountByCategoryCard, ticketTimeByCategoryCard);
		row2.setWidthFull();
		row2.setJustifyContentMode(JustifyContentMode.EVENLY);
		add(dateTimePickerRow, row1, row2);
		setSizeFull();
		update();
		
		toPicker.addValueChangeListener(event -> update());
		fromPicker.addValueChangeListener(event -> update());
	}
	
	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		final UI ui = attachEvent.getUI();
        broadcasterRegistration = Broadcaster.register(newMessage -> ui.access(() -> update()));
	}
	
	@Override
    protected void onDetach(DetachEvent detachEvent) {
        broadcasterRegistration.remove();
        broadcasterRegistration = null;
    }
	
	private void update() {
		final Date to = Date.from(this.toPicker.getValue().atZone(ZoneId.systemDefault()).toInstant());
		final Date from = Date.from(this.fromPicker.getValue().atZone(ZoneId.systemDefault()).toInstant());
		final List<Ticket> tickets = reportService.findByClosedTimestampAfterAndOpenTimestampBefore(from, to);
		ticketTimeByDepartmentCard.update(tickets);
		ticketCountByDepartmentCard.update(tickets);
		ticketCountByCategoryCard.update(tickets);
		ticketTimeByCategoryCard.update(tickets);
	}
	
}
