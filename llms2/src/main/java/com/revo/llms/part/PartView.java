package com.revo.llms.part;

import org.springframework.beans.factory.annotation.Autowired;

import com.revo.llms.common.MainLayout;
import com.revo.llms.common.TitledGridView;
import com.revo.llms.product.Product;
import com.revo.llms.product.ProductRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Parts")
@Route(value = "parts", layout = MainLayout.class)
public class PartView extends TitledGridView<Part> implements HasUrlParameter<Long> {
	private static final long serialVersionUID = 6769661703648507977L;
	
	private Product product;
	private final PartEditor editor;
	private final PartDataProvider dataProvider;
	private final PartRepository partRepository;
	private final ProductRepository productRepository;
	
	public PartView(@Autowired PartRepository partRepository, @Autowired ProductRepository productRepository) {
		super(VaadinIcon.COGS.create(), "Parts");
		this.partRepository = partRepository;
		this.productRepository = productRepository;
		this.dataProvider = new PartDataProvider(partRepository);
		this.editor = new PartEditor(partRepository, dataProvider);
		getGrid().setItems(dataProvider);
	}
	
	@Override
	public void setParameter(BeforeEvent event, Long productId) {
		dataProvider.setFilter(productId);
		this.product = productRepository.findById(productId).get();
		setTitle("Parts for " + product.getName());
	}
	
	@Override
	protected void createColumns(Grid<Part> grid) {
		grid.addColumn(Part::getId, "id").setHeader("Id");
		grid.addColumn(Part::getName, "name").setHeader("Name");
		super.createColumns(grid);
	}
	
	@Override
	protected void edit(Part value) {
		editor.open(product, value);
	}

	@Override
	protected void create() {
		editor.open(product, null);
	}

	@Override
	protected void delete(Part part) {
		partRepository.delete(part);
		dataProvider.refreshAll();
	}
}
