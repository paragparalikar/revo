package com.revo.llms.reason;

import java.util.stream.Stream;

import com.revo.llms.util.VaadinUtils;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReasonDataProvider<T> extends AbstractBackEndDataProvider<Reason, T> {
	private static final long serialVersionUID = 5244895494769624623L;

	@NonNull private final ReasonService reasonService;

	@Override
	protected Stream<Reason> fetchFromBackEnd(Query<Reason, T> query) {
		return reasonService.findAll(VaadinUtils.toPageable(query)).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<Reason, T> query) {
		return (int) reasonService.count();
	}
	
}
