package com.revo.llms.web.ui.vaadin.part;

import org.springframework.beans.factory.annotation.Autowired;

import com.revo.llms.part.Part;
import com.revo.llms.part.PartRepository;
import com.revo.llms.product.Product;
import com.revo.llms.product.ProductRepository;
import com.revo.llms.web.ui.vaadin.common.HasNameView;
import com.revo.llms.web.ui.vaadin.common.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Parts")
@Route(value = "parts", layout = MainLayout.class)
public class PartView extends HasNameView<Part> implements HasUrlParameter<Long> {
	private static final long serialVersionUID = 6769661703648507977L;
	
	private Product product;
	private final ProductRepository productRepository;
	
	public PartView(@Autowired PartRepository partRepository, @Autowired ProductRepository productRepository) {
		super("Parts", partRepository, new PartEditor(
				new PartDataProvider(partRepository).withConfigurableFilter(), 
				partRepository));
		this.productRepository = productRepository;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void setParameter(BeforeEvent event, Long productId) {
		final ConfigurableFilterDataProvider<Part, Void, Long> dataProvider = 
				(ConfigurableFilterDataProvider<Part, Void, Long>) getEditor().getDataProvider();
		dataProvider.setFilter(productId);
		this.product = productRepository.findById(productId).get();
		getTitle().setText("Parts for " + product.getName());
	}
	
	@Override
	protected void createColumns(Grid<Part> grid) {
		grid.addColumn(Part::getId).setHeader("Id");
		grid.addColumn(Part::getName, "name").setHeader("Name");
	}
	
	@Override
	protected void edit(Part value) {
		final PartEditor partEditor = (PartEditor) getEditor();
		partEditor.open(product, value);
	}
}
