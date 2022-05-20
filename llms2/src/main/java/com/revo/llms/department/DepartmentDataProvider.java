package com.revo.llms.department;

import java.util.stream.Stream;

import com.revo.llms.util.VaadinUtils;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class DepartmentDataProvider implements DataProvider<Department, Void> {
	private static final long serialVersionUID = -9093368755118927330L;

	@NonNull private final DepartmentService departmentService;
	@Delegate private final DataProvider<Department, Void> delegate = 
			DataProvider.fromCallbacks(this::findByQuery, this::countByQuery);
	
	private int countByQuery(Query<Department, Void> query) {
		return (int) departmentService.count();
	}

	private Stream<Department> findByQuery(Query<Department, Void> query) {
		return departmentService.findAll(VaadinUtils.toPageable(query)).stream();
	}
}
