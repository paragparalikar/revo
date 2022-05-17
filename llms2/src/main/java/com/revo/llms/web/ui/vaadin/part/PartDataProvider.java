package com.revo.llms.web.ui.vaadin.part;

import java.util.stream.Stream;

import com.revo.llms.part.Part;
import com.revo.llms.part.PartRepository;
import com.revo.llms.web.ui.vaadin.util.VaadinUtils;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class PartDataProvider implements DataProvider<Part, Long> {
	private static final long serialVersionUID = -1176660955950558817L;

	@NonNull private final PartRepository repository;
	@Delegate private final DataProvider<Part, Long> delegate = DataProvider.fromFilteringCallbacks(
			this::findByQuery, this::countByQuery);
	
	private int countByQuery(Query<Part, Long> query) {
		return repository.countByProductId(query.getFilter().get()).intValue();
	}

	private Stream<Part> findByQuery(Query<Part, Long> query) {
		return repository.findByProductId(query.getFilter().get(), VaadinUtils.toPageable(query)).stream();
	}

}
