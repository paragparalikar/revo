package com.revo.llms.web.ui.vaadin.product;

import org.springframework.beans.factory.annotation.Autowired;

import com.revo.llms.part.PartRepository;
import com.revo.llms.product.Product;
import com.revo.llms.product.ProductRepository;
import com.revo.llms.web.ui.vaadin.common.HasNameView;
import com.revo.llms.web.ui.vaadin.common.JpaDataProvider;
import com.revo.llms.web.ui.vaadin.common.MainLayout;
import com.revo.llms.web.ui.vaadin.part.PartView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Products")
@Route(value = "products", layout = MainLayout.class)
public class ProductView extends HasNameView<Product> {
	private static final long serialVersionUID = -597643178274272245L;

	private final PartRepository partRepository;
	
	public ProductView(@Autowired ProductRepository repository, @Autowired PartRepository partRepository) {
		super("Products", repository, new ProductEditor(new JpaDataProvider<>(repository), repository));
		this.partRepository = partRepository;
	}
	
	@Override
	protected void createColumns(Grid<Product> grid) {
		grid.addColumn(Product::getId).setHeader("Id");
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
		partRepository.deleteByProductId(product.getId());
		super.delete(product);
	}
}
