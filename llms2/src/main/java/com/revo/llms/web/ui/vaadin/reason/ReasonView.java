package com.revo.llms.web.ui.vaadin.reason;

import org.springframework.beans.factory.annotation.Autowired;

import com.revo.llms.reason.Reason;
import com.revo.llms.reason.ReasonRepository;
import com.revo.llms.web.ui.vaadin.common.HasNameView;
import com.revo.llms.web.ui.vaadin.common.JpaDataProvider;
import com.revo.llms.web.ui.vaadin.common.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Reasons")
@Route(value = "reasons", layout = MainLayout.class)
public class ReasonView extends HasNameView<Reason> {
	private static final long serialVersionUID = 7405219789236846586L;
	
	public ReasonView(@Autowired ReasonRepository repository) {
		super(VaadinIcon.EXCLAMATION_CIRCLE.create(), "Reasons", repository, 
				new ReasonEditor(new JpaDataProvider<>(repository), repository));
	}

	@Override
	protected void createColumns(Grid<Reason> grid) {
		grid.addColumn(Reason::getId).setHeader("Id");
		super.createColumns(grid);
	}
	
}
