package com.revo.llms.dashboard;

import javax.annotation.security.PermitAll;

import com.revo.llms.LlmsConstants;
import com.revo.llms.common.Broadcaster;
import com.revo.llms.common.Broadcaster.Registration;
import com.revo.llms.common.MainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
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

	private Registration broadcasterRegistration;
	private final TotalCountCard totalCountCard;
	private final CountByDepartmentCard countByDepartmentCard;
	private final TodaysTicketStatusByDepartmentCard todaysTicketStatusByDepartmentCard;
	private final HorizontalLayout row1 = new HorizontalLayout();
	
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
	
	public DashboardView(DashboardService dashboardService) {
		this.totalCountCard = new TotalCountCard(dashboardService);
		this.countByDepartmentCard = new CountByDepartmentCard(dashboardService);
		this.todaysTicketStatusByDepartmentCard = new TodaysTicketStatusByDepartmentCard(dashboardService);
		
		row1.setWidthFull();
		row1.setJustifyContentMode(JustifyContentMode.BETWEEN);
		row1.add(totalCountCard, countByDepartmentCard, todaysTicketStatusByDepartmentCard);

		add(row1);
	}
	
	private void update() {
		totalCountCard.update();
		countByDepartmentCard.update();
		todaysTicketStatusByDepartmentCard.update();
	}
	
}
