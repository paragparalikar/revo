package com.revo.llms.part;

import java.util.stream.Stream;

import com.revo.llms.product.Product;
import com.revo.llms.util.VaadinUtils;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class PartDataProvider implements ConfigurableFilterDataProvider<Part, Void, Long> {
	private static final long serialVersionUID = -1176660955950558817L;

	@Setter private Product product;
	@NonNull private final PartRepository repository;
	@Delegate private final ConfigurableFilterDataProvider<Part, Void, Long> delegate = 
			DataProvider.fromFilteringCallbacks(this::findByQuery, this::countByQuery)
			.withConfigurableFilter();
	
	private int countByQuery(Query<Part, Long> query) {
		if(null == product) return 0;
		return repository.countByProductId(product.getId()).intValue();
	}

	private Stream<Part> findByQuery(Query<Part, Long> query) {
		if(null == product) return Stream.empty();
		return repository.findByProductId(product.getId(), VaadinUtils.toPageable(query)).stream();
	}

}
