	package com.revo.llms;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.revo.llms.department.Department;
import com.revo.llms.department.DepartmentRepository;
import com.revo.llms.part.Part;
import com.revo.llms.part.PartRepository;
import com.revo.llms.product.Product;
import com.revo.llms.product.ProductRepository;
import com.revo.llms.reason.Reason;
import com.revo.llms.reason.ReasonRepository;
import com.revo.llms.ticket.Ticket;
import com.revo.llms.ticket.TicketRepository;
import com.revo.llms.ticket.TicketStatus;

import lombok.RequiredArgsConstructor;

@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class DevConfiguration {

	private final TicketRepository ticketRepository;
	private final DepartmentRepository departmentRepository;
	private final ReasonRepository reasonRepository;
	private final ProductRepository productRepository;
	private final PartRepository partRepository;
	
	private void createDepartments() {
		if(0 == departmentRepository.count()) {
			for(int index = 0; index < 4; index++) {
				final Department department = new Department();
				department.setCode(index);
				department.setName("Department-"+index);
				departmentRepository.save(department);
			}
		}
	}
	
	private void createReasons() {
		if(0 == reasonRepository.count()) {
			for(int index = 1; index <= 5; index++) {
				final Reason reason = new Reason();
				reason.setText("Reason-"+index);
				reasonRepository.save(reason);
			}
		}
	}
	
	private void createProducts() {
		if(0 == productRepository.count()) {
			for(int index = 1; index <= 2; index++) {
				final Product product = new Product();
				product.setName("Product-"+index);
				productRepository.save(product);
			}
		}
	}
	
	private void createParts() {
		if(0 == partRepository.count()) {
			for(Product product : productRepository.findAll()) {
				for(int index = 1; index <= 100; index++) {
					final Part part = new Part(null, "Part-"+index, product);
					partRepository.save(part);
				}
			}
		}
	}
	
	private void createTickets() {
		if(0 == ticketRepository.count()) {
			final List<Part> parts = partRepository.findAll();
			for(Department department : departmentRepository.findAll()) {
				for(Reason reason: reasonRepository.findAll()) {
					for(int stationId = 1; stationId <= 30; stationId++) {
						final int count = (int) (Math.random() * 10);
						final Calendar calendar = Calendar.getInstance();
						calendar.add(Calendar.DATE, -1 * count);
						for(int index = 0; index < count; index++) {
							final Ticket ticket = new Ticket();
							ticket.setStationId(stationId);
							ticket.setStatus(TicketStatus.OPEN);
							ticket.setOpenTimestamp(calendar.getTime());
							ticket.setDepartment(department);
							if(0 == index % 2) {
								ticket.setStatus(TicketStatus.CLOSED);
								ticket.setClosedTimestamp(new Date());
								ticket.setReason(reason);
								ticket.setPart(parts.get(index % parts.size()));
							}
							ticketRepository.save(ticket);
						}
					}
				}
			}
		}
	}
	
	@Bean
	public CommandLineRunner dataSetup() {
		return args -> {
			createDepartments();
			createReasons();
			createProducts();
			createParts();
		//	createTickets();
		};
	}
	
}
