package com.revo.llms.category;

import java.util.stream.Stream;

import com.revo.llms.util.VaadinUtils;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryDataProvider<T> extends AbstractBackEndDataProvider<Category, T> {
	private static final long serialVersionUID = 1L;

	private final CategoryService categoryService;
	
	@Override
	protected Stream<Category> fetchFromBackEnd(Query<Category, T> query) {
		return categoryService.findAll(VaadinUtils.toPageable(query)).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<Category, T> query) {
		return (int) categoryService.count();
	}

}
