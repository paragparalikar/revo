package com.revo.llms.product;

import java.util.stream.Stream;

import com.revo.llms.util.VaadinUtils;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class ProductDataProvider implements DataProvider<Product, Void> {
	private static final long serialVersionUID = -3214093958984183834L;

	@NonNull private final ProductService productService;
	@Delegate private final DataProvider<Product, Void> delegate = 
			DataProvider.fromCallbacks(this::findByQuery, this::countByQuery);
			
	private int countByQuery(Query<Product, Void> query) {
		return (int) productService.count();
	}

	private Stream<Product> findByQuery(Query<Product, Void> query) {
		return productService.findAll(VaadinUtils.toPageable(query)).stream();
	}
	
}
