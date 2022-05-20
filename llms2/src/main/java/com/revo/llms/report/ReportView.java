package com.revo.llms.report;

import static com.revo.llms.LlmsConstants.ROUTE_REPORTS;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;

import com.revo.llms.common.MainLayout;
import com.revo.llms.ticket.Ticket;
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
	private final TicketTimeByReasonCard ticketTimeByReasonCard;
	private final TicketCountByReasonCard ticketCountByReasonCard;
	private final TicketCountByDepartmentCard ticketCountByDepartmentCard;
	private final TicketTimeByDepartmentCard ticketTimeByDepartmentCard;
	private final DateTimePicker toPicker = new DateTimePicker("To", LocalDateTime.now());
	private final DateTimePicker fromPicker = new DateTimePicker("From", LocalDateTime.now().minusMonths(1));
	private final HorizontalLayout dateTimePickerRow = new HorizontalLayout(fromPicker, toPicker);
	private final HorizontalLayout row1 = new HorizontalLayout();
	private final HorizontalLayout row2 = new HorizontalLayout();
	
	public ReportView(ReportService reportService) {
		this.reportService = reportService;
		add(dateTimePickerRow);
		dateTimePickerRow.setWidthFull();
		dateTimePickerRow.setJustifyContentMode(JustifyContentMode.CENTER);
		setSizeFull();
		this.ticketTimeByReasonCard = new TicketTimeByReasonCard(reportService);
		this.ticketCountByReasonCard = new TicketCountByReasonCard(reportService);
		this.ticketTimeByDepartmentCard = new TicketTimeByDepartmentCard(reportService);
		this.ticketCountByDepartmentCard = new TicketCountByDepartmentCard(reportService);
		
		row1.add(ticketCountByDepartmentCard, ticketTimeByDepartmentCard);
		row1.setWidthFull();
		row1.setJustifyContentMode(JustifyContentMode.EVENLY);
		
		row2.add(ticketCountByReasonCard, ticketTimeByReasonCard);
		row2.setWidthFull();
		row2.setJustifyContentMode(JustifyContentMode.EVENLY);
		add(row1, row2);
		update();
	}
	
	private void update() {
		final Date to = Date.from(this.toPicker.getValue().atZone(ZoneId.systemDefault()).toInstant());
		final Date from = Date.from(this.fromPicker.getValue().atZone(ZoneId.systemDefault()).toInstant());
		final List<Ticket> tickets = reportService.findByClosedTimestampAfterAndOpenTimestampBefore(from, to);
		ticketTimeByDepartmentCard.update(tickets);
		ticketCountByDepartmentCard.update(tickets);
		ticketCountByReasonCard.update(tickets);
		ticketTimeByReasonCard.update(tickets);
	}
	
}
