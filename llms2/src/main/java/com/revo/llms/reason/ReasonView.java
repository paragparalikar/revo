package com.revo.llms.reason;

import javax.annotation.security.PermitAll;

import com.revo.llms.LlmsConstants;
import com.revo.llms.category.CategoryService;
import com.revo.llms.common.MainLayout;
import com.revo.llms.common.TitledGridView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.codecamp.vaadin.security.spring.access.SecuredAccess;

@PermitAll
@PageTitle("Reasons")
@SecuredAccess("hasAuthority('page-reasons')")
@Route(value = LlmsConstants.ROUTE_REASONS, layout = MainLayout.class)
public class ReasonView extends TitledGridView<Reason> {
	private static final long serialVersionUID = 7405219789236846586L;
	
	private final ReasonEditor editor;
	private final ReasonService reasonService;
	private final DataProvider<Reason, Void> dataProvider;
	
	public ReasonView(ReasonService reasonService, CategoryService categoryService) {
		super(VaadinIcon.EXCLAMATION_CIRCLE.create(), "Reasons");
		this.reasonService = reasonService;
		this.dataProvider = new ReasonDataProvider<Void>(reasonService);
		this.editor = new ReasonEditor(reasonService, categoryService, dataProvider);
		final Grid<Reason> grid = new Grid<>();
		grid.setItems(dataProvider);
		createColumns(grid);
		add(grid, editor);
	}

	protected void createColumns(Grid<Reason> grid) {
		grid.addColumn(Reason::getId, "id").setHeader("Id");
		grid.addColumn(reason -> reason.getCategory().getName(), "category.name").setHeader("Category");
		grid.addColumn(Reason::getText, "text").setHeader("Text");
		super.createColumns(grid);
	}

	@Override
	protected void create() {
		editor.open(null);
	}

	@Override
	protected void edit(Reason value) {
		editor.open(value);
	}

	@Override
	protected void delete(Reason value) {
		reasonService.delete(value);
		dataProvider.refreshAll();
	}
	
}
