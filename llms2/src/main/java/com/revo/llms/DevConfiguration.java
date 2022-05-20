package com.revo.llms;

import static com.revo.llms.LlmsConstants.ROUTE_DASHBOARD;
import static com.revo.llms.LlmsConstants.ROUTE_DEPARTMENTS;
import static com.revo.llms.LlmsConstants.ROUTE_PARTS;
import static com.revo.llms.LlmsConstants.ROUTE_PRODUCTS;
import static com.revo.llms.LlmsConstants.ROUTE_REASONS;
import static com.revo.llms.LlmsConstants.ROUTE_TICKETS;
import static com.revo.llms.LlmsConstants.ROUTE_USERS;
import static com.revo.llms.LlmsConstants.ROUTE_REPORTS;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.revo.llms.user.User;
import com.revo.llms.user.UserService;

import lombok.RequiredArgsConstructor;

@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class DevConfiguration {

	private final UserService userService;
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
			for(int index = 1; index <= 100; index++) {
				for(Product product : products) {
					final Part part = new Part(null, "Part-"+index, product);
					final Part result = partRepository.save(part);
					parts.add(result);
				}
			}
			
			for(Department department : departments) {
				for(Reason reason: reasons) {
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
			
			final User user = new User();
			user.setUsername("admin");
			user.setPassword("admin");
			user.getDepartments().addAll(departments);
			user.getPages().addAll(Arrays.asList(ROUTE_DASHBOARD,
		            		ROUTE_TICKETS,
		            		ROUTE_DEPARTMENTS,
		            		ROUTE_REASONS,
		            		ROUTE_PRODUCTS,
		            		ROUTE_USERS, 
		            		ROUTE_REPORTS,
		            		ROUTE_PARTS));
			userService.save(user);
		};
	}
	
}
