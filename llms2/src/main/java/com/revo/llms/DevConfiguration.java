	package com.revo.llms;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.revo.llms.category.Category;
import com.revo.llms.category.CategoryRepository;
import com.revo.llms.department.Department;
import com.revo.llms.department.DepartmentRepository;
import com.revo.llms.part.Part;
import com.revo.llms.part.PartRepository;
import com.revo.llms.product.Product;
import com.revo.llms.product.ProductRepository;
import com.revo.llms.reason.Reason;
import com.revo.llms.reason.ReasonRepository;
import com.revo.llms.station.StationService;
import com.revo.llms.ticket.Ticket;
import com.revo.llms.ticket.TicketRepository;
import com.revo.llms.ticket.TicketStatus;
import com.revo.llms.user.UserRepository;

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
	private final UserRepository userRepository;
	private final StationService stationService;
	private final CategoryRepository categoryRepository;
	
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
	
	private void createCategories() {
		if(0 == categoryRepository.count()) {
			for(int index = 1; index < 5; index++) {
				final Category category = new Category();
				category.setName("Category-"+index);
				categoryRepository.saveAndFlush(category);
			}
		}
	}
	
	private void createReasons() {
		if(0 == reasonRepository.count()) {
			for(Category category : categoryRepository.findAll()) {
				for(int index = 1; index <= 5; index++) {
					final Reason reason = new Reason();
					reason.setCategory(category);
					reason.setText("Reason-"+index+"-"+category.getName());
					reasonRepository.save(reason);
				}
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
							ticket.setStation(stationService.findById(stationId).get());
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
		} else {
			int counter = 0;
			for(Ticket ticket : ticketRepository.findAll()) {
				if(++counter % 2 == 0) {
					ticket.setStatus(TicketStatus.OPEN);
				} else {
					ticket.setStatus(TicketStatus.CLOSED);
				}
				ticketRepository.save(ticket);
			}
		}
	}
	
	private void authorizeUsers() {
		userRepository.findAll().forEach(user -> {
			user.getDepartments().addAll(departmentRepository.findAll());
			user.getPages().addAll(Arrays.asList(
					LlmsConstants.ROUTE_DASHBOARD,
					LlmsConstants.ROUTE_DEPARTMENTS,
					LlmsConstants.ROUTE_PARTS,
					LlmsConstants.ROUTE_PRODUCTS,
					LlmsConstants.ROUTE_REASONS,
					LlmsConstants.ROUTE_REPORTS,
					LlmsConstants.ROUTE_TICKETS,
					LlmsConstants.ROUTE_USERS,
					LlmsConstants.ROUTE_STATIONS,
					LlmsConstants.ROUTE_CATEGORIES
					));
			userRepository.save(user);
		});
	}
	
	@Bean
	public CommandLineRunner dataSetup() {
		return args -> {
			createDepartments();
			createCategories();
			createReasons();
			createProducts();
			createParts();
			createTickets();
			authorizeUsers();
		};
	}
	
}
