package com.revo.llms.category;

import javax.annotation.security.PermitAll;

import com.revo.llms.LlmsConstants;
import com.revo.llms.common.MainLayout;
import com.revo.llms.common.TitledGridView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.codecamp.vaadin.security.spring.access.SecuredAccess;

@PermitAll
@PageTitle("Reason Categories")
@SecuredAccess("hasAuthority('page-categories')")
@Route(value = LlmsConstants.ROUTE_CATEGORIES, layout = MainLayout.class)
public class CategoryView extends TitledGridView<Category> {
	private static final long serialVersionUID = 1L;

	private final CategoryEditor categoryEditor;
	private final CategoryService categoryService;
	private final ListDataProvider<Category> dataProvider;
	
	public CategoryView(CategoryService categoryService) {
		super(VaadinIcon.SPLIT.create(), "Reason Categories");
		this.categoryService = categoryService;
		this.dataProvider = new ListDataProvider<>(categoryService.findAll());
		this.categoryEditor = new CategoryEditor(categoryService, dataProvider);
		final Grid<Category> grid = new Grid<>();
		grid.setItems(dataProvider);
		createColumns(grid);
		add(grid);
	}
	
	protected void createColumns(Grid<Category> grid) {
		grid.addColumn(Category::getId, "id").setHeader("Id");
		grid.addColumn(Category::getName, "name").setHeader("Name");
		super.createColumns(grid);
	}

	@Override
	protected void create() {
		categoryEditor.open(null);
	}

	@Override
	protected void edit(Category value) {
		categoryEditor.open(value);
	}

	@Override
	protected void delete(Category value) {
		categoryService.delete(value);
		dataProvider.refreshAll();
	}

}
