package com.revo.llms.part;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.annotation.security.PermitAll;

import org.apache.commons.io.IOUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.vaadin.klaudeta.PaginatedGrid;

import com.revo.llms.LlmsConstants;
import com.revo.llms.common.MainLayout;
import com.revo.llms.common.TitledGridView;
import com.revo.llms.product.Product;
import com.revo.llms.product.ProductService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.SneakyThrows;

@PermitAll
@PageTitle("Parts")
@Route(value = LlmsConstants.ROUTE_PARTS, layout = MainLayout.class)
public class PartView extends TitledGridView<Part> implements HasUrlParameter<Long> {
	private static final long serialVersionUID = 6769661703648507977L;
	
	private Product product;
	private final PartEditor editor;
	private final PartService partService;
	private final ProductService productService;
	private final PartDataProvider dataProvider;
	
	public PartView(PartService partService, ProductService productSerive) {
		super(VaadinIcon.COGS.create(), "Parts");
		this.partService = partService;
		this.productService = productSerive;
		this.dataProvider = new PartDataProvider(partService);
		this.editor = new PartEditor(partService, dataProvider);
		final PaginatedGrid<Part> grid = new PaginatedGrid<>();
		grid.setItems(dataProvider);
		grid.setPageSize(10);
		grid.setPaginatorSize(5);
		createColumns(grid);
		addRight(createUploadButton());
		add(grid, editor);
	}
	
	private Component createUploadButton() {
		final MemoryBuffer receiver = new MemoryBuffer();
		final Upload upload = new Upload(receiver);
		upload.setAcceptedFileTypes("text/csv", "application/vnd.ms-excel");
		upload.setMaxFileSize(10485760);
		upload.setUploadButton(new Button("Upload", VaadinIcon.UPLOAD.create()));
		upload.addSucceededListener(event -> upload(receiver.getInputStream()));
		return upload;
	}
	
	@SneakyThrows
	private void upload(InputStream is) {
		for(String line : IOUtils.readLines(is, StandardCharsets.UTF_8)) {
			final String[] tokens = line.split(",");
			final Part part = new Part(Long.parseLong(tokens[0]), tokens[1], product);
			partService.save(part);
		}
		dataProvider.refreshAll();
	}
	
	@Override
	public void setParameter(BeforeEvent event, Long productId) {
		this.product = productService.findById(productId).get();
		setTitle("Parts for " + product.getName());
		dataProvider.setProduct(product);
		dataProvider.refreshAll();
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
		try {
			partService.delete(part);
			dataProvider.refreshAll();
		} catch(DataIntegrityViolationException dive) {
			setError("There are tickets associated with this part, thus part can not be deleted");
		}
	}
}
