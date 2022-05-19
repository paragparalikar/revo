package com.revo.llms.dashboard;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;

import com.revo.llms.LlmsConstants;
import com.revo.llms.common.MainLayout;
import com.revo.llms.department.DepartmentRepository;
import com.revo.llms.ticket.TicketRepository;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PermitAll
@PageTitle("Dashboard")
@Route(value = LlmsConstants.ROUTE_DASHBOARD, layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {
	private static final long serialVersionUID = -9207529279894406046L;

	private final TotalCountCard totalCountCard;
	private final CountByDepartmentCard countByDepartmentCard;
	private final TodaysTicketStatusByDepartmentCard todaysTicketStatusByDepartmentCard;
	private final HorizontalLayout row1 = new HorizontalLayout();
	
	public DashboardView(@Autowired TicketRepository repository,
			@Autowired DepartmentRepository departmentRepository) {
		this.totalCountCard = new TotalCountCard(repository);
		this.countByDepartmentCard = new CountByDepartmentCard(repository);
		this.todaysTicketStatusByDepartmentCard = new TodaysTicketStatusByDepartmentCard(repository, departmentRepository);
		add(row1);
		row1.add(totalCountCard, countByDepartmentCard, todaysTicketStatusByDepartmentCard);
	}
	
}
