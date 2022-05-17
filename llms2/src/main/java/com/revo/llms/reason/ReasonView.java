package com.revo.llms.reason;

import org.springframework.beans.factory.annotation.Autowired;

import com.revo.llms.common.JpaDataProvider;
import com.revo.llms.common.MainLayout;
import com.revo.llms.common.TitledGridView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Reasons")
@Route(value = "reasons", layout = MainLayout.class)
public class ReasonView extends TitledGridView<Reason> {
	private static final long serialVersionUID = 7405219789236846586L;
	
	private final ReasonEditor editor;
	private final ReasonRepository repository;
	private final DataProvider<Reason, Void> dataProvider;
	
	public ReasonView(@Autowired ReasonRepository repository) {
		super(VaadinIcon.EXCLAMATION_CIRCLE.create(), "Reasons");
		this.repository = repository;
		this.dataProvider = new JpaDataProvider<>(repository);
		this.editor = new ReasonEditor(repository, dataProvider);
		final Grid<Reason> grid = new Grid<>();
		grid.setItems(dataProvider);
		createColumns(grid);
		add(grid, editor);
	}

	protected void createColumns(Grid<Reason> grid) {
		grid.addColumn(Reason::getId, "id").setHeader("Id");
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
		repository.delete(value);
		dataProvider.refreshAll();
	}
	
}
