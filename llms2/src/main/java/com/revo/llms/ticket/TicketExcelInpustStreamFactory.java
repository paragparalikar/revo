package com.revo.llms.ticket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.revo.llms.LlmsConstants;
import com.revo.llms.category.Category;
import com.revo.llms.common.security.SecurityService;
import com.revo.llms.department.Department;
import com.revo.llms.part.Part;
import com.revo.llms.product.Product;
import com.revo.llms.reason.Reason;
import com.revo.llms.station.Station;
import com.vaadin.flow.server.InputStreamFactory;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Builder
@RequiredArgsConstructor
public class TicketExcelInpustStreamFactory implements InputStreamFactory {
	private static final long serialVersionUID = 1L;

	private final TicketService ticketService;
	private final SecurityService securityService;
	private final Supplier<LocalDate> toDateProvider;
	private final Supplier<LocalDate> fromDateProvider;
	private final DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a");
	
	@Override
	@SneakyThrows
	public InputStream createInputStream() {
		final HSSFWorkbook book = new HSSFWorkbook();
		final HSSFSheet sheet = book.createSheet();
		final UserDetails user = securityService.getAuthenticatedUser();
		final Set<Long> departmentIds = resolve(user, LlmsConstants.PREFIX_DEPARTMENT);
		final Date to = Date.from(toDateProvider.get().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		final Date from = Date.from(fromDateProvider.get().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		final List<Ticket> tickets = ticketService.findByDepartmentIdInAndOpenTimestampBetween(
				departmentIds, from, to);
		createHeaders(sheet.createRow(0));
		for(int index = 0; index < tickets.size(); index++) {
			createRow(tickets.get(index), sheet.createRow(index + 1));
		}
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		book.write(outputStream);
		outputStream.close();
		return new ByteArrayInputStream(outputStream.toByteArray());
	}
	
	private void createRow(Ticket ticket, HSSFRow row) {
		row.createCell(0, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(Optional.ofNullable(ticket)
				.map(Ticket::getId).orElse(null));
		row.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(Optional.ofNullable(ticket)
				.map(Ticket::getStatus).map(TicketStatus::name).orElse(null));
		row.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(Optional.ofNullable(ticket)
				.map(Ticket::getStation).map(Station::getName).orElse(null));
		row.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(Optional.ofNullable(ticket)
				.map(Ticket::getDepartment).map(Department::getName).orElse(null));
		row.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(Optional.ofNullable(ticket)
				.map(Ticket::getOpenTimestamp).map(dateFormat::format).orElse(null));
		row.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(Optional.ofNullable(ticket)
				.map(Ticket::getClosedTimestamp).map(dateFormat::format).orElse(null));
		row.createCell(6, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(Optional.ofNullable(ticket)
				.map(Ticket::getLossInMinutes).orElse(null));
		row.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue(Optional.ofNullable(ticket)
				.map(Ticket::getReason).map(Reason::getCategory).map(Category::getName).orElse(null));
		row.createCell(8, HSSFCell.CELL_TYPE_STRING).setCellValue(Optional.ofNullable(ticket)
				.map(Ticket::getReason).map(Reason::getText).orElse(null));
		row.createCell(9, HSSFCell.CELL_TYPE_STRING).setCellValue(Optional.ofNullable(ticket)
				.map(Ticket::getPart).map(Part::getProduct).map(Product::getName).orElse(null));
		row.createCell(10, HSSFCell.CELL_TYPE_STRING).setCellValue(Optional.ofNullable(ticket)
				.map(Ticket::getPart).map(Part::getName).orElse(null));
	}
	
	private void createHeaders(HSSFRow row) {
		row.createCell(0).setCellValue("No.");
		row.createCell(1).setCellValue("Status");
		row.createCell(2).setCellValue("Station");
		row.createCell(3).setCellValue("Department");
		row.createCell(4).setCellValue("Open");
		row.createCell(5).setCellValue("Closed");
		row.createCell(6).setCellValue("Loss(Hrs)");
		row.createCell(7).setCellValue("Reason Category");
		row.createCell(8).setCellValue("Reason");
		row.createCell(9).setCellValue("Product");
		row.createCell(10).setCellValue("Part");
	}
	
	private Set<Long> resolve(UserDetails user, String prefix){
		return user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.filter(text -> text.startsWith(prefix))
				.map(text -> text.substring(prefix.length()))
				.map(Long::parseLong)
				.collect(Collectors.toSet());
	}

}
