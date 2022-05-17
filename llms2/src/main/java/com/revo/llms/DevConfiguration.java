package com.revo.llms;

import java.util.ArrayList;
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
	
	@Bean
	public CommandLineRunner dataSetup() {
		return args -> {
			final List<Part> parts = new ArrayList<>();
			final List<Reason> reasons = new ArrayList<>();
			final List<Product> products = new ArrayList<>();
			final List<Department> departments = new ArrayList<>();
			for(int index = 1; index <= 4; index++) {
				final Department department = new Department();
				department.setCode(index);
				department.setName("Department-"+index);
				final Department result = departmentRepository.save(department);
				departments.add(result);
			}
			for(int index = 1; index <= 5; index++) {
				final Reason reason = new Reason();
				reason.setText("Reason-"+index);
				final Reason result = reasonRepository.save(reason);
				reasons.add(result);
			}
			for(int index = 1; index <= 2; index++) {
				final Product product = new Product();
				product.setName("Product-"+index);
				final Product result = productRepository.save(product);
				products.add(result);
			}
			for(int index = 1; index <= 2000; index++) {
				for(Product product : products) {
					final Part part = new Part(null, "Part-"+index, product);
					final Part result = partRepository.save(part);
					parts.add(result);
				}
			}
			for(int index = 1; index <= 100; index++) {
				final Ticket ticket = new Ticket();
				ticket.setStationId(2);
				ticket.setStatus(TicketStatus.OPEN);
				ticket.setOpenTimestamp(new Date());
				ticket.setDepartment(departments.get(index % departments.size()));
				if(0 == index % 2) {
					ticket.setStatus(TicketStatus.CLOSED);
					ticket.setClosedTimestamp(new Date());
					ticket.setReason(reasons.get(index % reasons.size()));
					ticket.setPart(parts.get(index % parts.size()));
				}
				ticketRepository.save(ticket);
			}
		};
	}
	
}
