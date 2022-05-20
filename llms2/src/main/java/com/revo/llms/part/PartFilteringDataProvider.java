package com.revo.llms.part;

import java.util.stream.Stream;

import com.revo.llms.product.Product;
import com.revo.llms.util.VaadinUtils;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class PartFilteringDataProvider implements DataProvider<Part, String> {
	private static final long serialVersionUID = -3103664511127255983L;

	@Setter private Product product;
	@NonNull private final PartService partService;
	@Delegate private final DataProvider<Part, String> delegate = 
			DataProvider.fromFilteringCallbacks(this::findByQuery, this::countByQuery);
	
	private int countByQuery(Query<Part, String> query) {
		if(null == product) return 0;
		return partService.countByProductId(product.getId()).intValue();
	}

	private Stream<Part> findByQuery(Query<Part, String> query) {
		if(null == product) return Stream.empty();
		return partService.findByProductId(product.getId(), VaadinUtils.toPageable(query)).stream();
	}

			
}
