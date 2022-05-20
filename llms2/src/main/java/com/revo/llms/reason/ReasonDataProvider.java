package com.revo.llms.reason;

import java.util.stream.Stream;

import com.revo.llms.util.VaadinUtils;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class ReasonDataProvider implements DataProvider<Reason, Void> {
	private static final long serialVersionUID = 5244895494769624623L;

	@NonNull private final ReasonService reasonService;
	@Delegate private final DataProvider<Reason, Void> delegate = 
			DataProvider.fromCallbacks(this::findByQuery, this::countByQuery);
			
	private int countByQuery(Query<Reason, Void> query) {
		return (int) reasonService.count();
	}

	private Stream<Reason> findByQuery(Query<Reason, Void> query) {
		return reasonService.findAll(VaadinUtils.toPageable(query)).stream();
	}
	
}
