package com.revo.llms.reason;

import java.util.stream.Stream;

import com.revo.llms.util.VaadinUtils;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class ReasonFilteringDataProvider implements DataProvider<Reason, String> {
	private static final long serialVersionUID = 5244895494769624623L;

	@NonNull private final ReasonService reasonService;
	@Delegate private final DataProvider<Reason, String> delegate = 
			DataProvider.fromFilteringCallbacks(this::findByQuery, this::countByQuery);
			
	private int countByQuery(Query<Reason, String> query) {
		return (int) reasonService.count();
	}

	private Stream<Reason> findByQuery(Query<Reason, String> query) {
		return reasonService.findAll(VaadinUtils.toPageable(query)).stream();
	}
}
