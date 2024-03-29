package com.revo.llms.product;

import javax.annotation.security.PermitAll;

import org.springframework.dao.DataIntegrityViolationException;

import com.revo.llms.LlmsConstants;
import com.revo.llms.common.MainLayout;
import com.revo.llms.common.TitledGridView;
import com.revo.llms.part.PartView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.codecamp.vaadin.security.spring.access.SecuredAccess;

@PermitAll
@PageTitle("Products")
@SecuredAccess("hasAuthority('page-products')")
@Route(value = LlmsConstants.ROUTE_PRODUCTS, layout = MainLayout.class)
public class ProductView extends TitledGridView<Product> {
	private static final long serialVersionUID = -597643178274272245L;

	private final ProductEditor editor;
	private final ProductService productService;
	private final DataProvider<Product, Void> dataProvider;
	
	public ProductView(ProductService productService) {
		super(VaadinIcon.CART.create(), "Products");
		this.productService = productService;
		this.dataProvider = new ProductDataProvider<Void>(productService);
		this.editor = new ProductEditor(productService, dataProvider);
		final Grid<Product> grid = new Grid<>();
		grid.setItems(dataProvider);
		createColumns(grid);
		add(grid, editor);
	}
	
	@Override
	protected void createColumns(Grid<Product> grid) {
		grid.addColumn(Product::getId, "id").setHeader("Id");
		grid.addColumn(Product::getName, "name").setHeader("Name");
		super.createColumns(grid);
	}
	
	@Override
	protected HorizontalLayout createActionCell(Product value) {
		final HorizontalLayout cell = super.createActionCell(value);
		final Button partsButton = new Button("Parts", VaadinIcon.COGS.create());
		partsButton.addClickListener(event -> {
			partsButton.getUI().get().navigate(PartView.class, value.getId());
		});
		cell.addComponentAsFirst(partsButton);
		return cell;
	}
	
	@Override
	protected void delete(Product product) {
		try {
			productService.deleteById(product.getId());
			dataProvider.refreshAll();
		} catch(DataIntegrityViolationException dive) {
			setError("There are tickets associated with this product, thus product can not be deleted");
		}
	}

	@Override
	protected void create() {
		editor.open(null);
	}

	@Override
	protected void edit(Product value) {
		editor.open(value);
	}
}
