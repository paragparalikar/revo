package com.revo.llms.product;

import java.util.stream.Stream;

import com.revo.llms.util.VaadinUtils;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductDataProvider<T> extends AbstractBackEndDataProvider<Product, T> {
	private static final long serialVersionUID = -3214093958984183834L;

	@NonNull private final ProductService productService;
	
	@Override
	protected Stream<Product> fetchFromBackEnd(Query<Product, T> query) {
		return productService.findAll(VaadinUtils.toPageable(query)).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<Product, T> query) {
		return (int) productService.count();
	}
	
}
