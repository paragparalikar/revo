package com.revo.llms.user;

import java.util.stream.Stream;

import com.revo.llms.util.VaadinUtils;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class UserDataProvider implements DataProvider<User, Void> {
	private static final long serialVersionUID = -3874147678410599726L;

	@NonNull private final UserService userService;
	@Delegate private final DataProvider<User, Void> delegate = 
			DataProvider.fromCallbacks(this::findByQuery, this::countByQuery);
			
	private int countByQuery(Query<User, Void> query) {
		return (int) userService.count();
	}

	private Stream<User> findByQuery(Query<User, Void> query) {
		return userService.findAll(VaadinUtils.toPageable(query)).stream();
	}
}
