package com.revo.llms.web.ui.vaadin.part;

import java.util.stream.Stream;

import com.revo.llms.part.Part;
import com.revo.llms.part.PartRepository;
import com.revo.llms.web.ui.vaadin.util.VaadinUtils;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class PartDataProvider implements ConfigurableFilterDataProvider<Part, Void, Long> {
	private static final long serialVersionUID = -1176660955950558817L;

	@NonNull private final PartRepository repository;
	@Delegate private final ConfigurableFilterDataProvider<Part, Void, Long> delegate = 
			DataProvider.fromFilteringCallbacks(this::findByQuery, this::countByQuery)
			.withConfigurableFilter();
	
	private int countByQuery(Query<Part, Long> query) {
		return repository.countByProductId(query.getFilter().get()).intValue();
	}

	private Stream<Part> findByQuery(Query<Part, Long> query) {
		return repository.findByProductId(query.getFilter().get(), VaadinUtils.toPageable(query)).stream();
	}

}
