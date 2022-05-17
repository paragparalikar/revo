package com.revo.llms.common;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revo.llms.util.VaadinUtils;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class JpaDataProvider<T, F> implements DataProvider<T, F> {
	private static final long serialVersionUID = -2407236386318050172L;

	@NonNull private final JpaRepository<T, ?> repository;
	@Delegate private final DataProvider<T, F> delegate = new CallbackDataProvider<>(this::fetch_, this::count_);
	
	private int count_(Query<T, F> query) {
		return (int) repository.count();
	}

	private Stream<T> fetch_(Query<T, F> query) {
		return repository.findAll(VaadinUtils.toPageable(query)).stream();
	}
	
}
