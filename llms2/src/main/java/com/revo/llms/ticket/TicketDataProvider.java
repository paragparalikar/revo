package com.revo.llms.ticket;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.revo.llms.LlmsConstants;
import com.revo.llms.common.security.SecurityService;
import com.revo.llms.util.VaadinUtils;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class TicketDataProvider implements DataProvider<Ticket, Void> {
	private static final long serialVersionUID = -6035690994345301935L;

	@NonNull private final TicketRepository repository;
	@NonNull private final SecurityService securityService;
	@Delegate private final DataProvider<Ticket, Void> delegate = new CallbackDataProvider<>(this::fetch_, this::count_);
	
	private int count_(Query<Ticket, Void> query) {
		return (int) load(query).getTotalElements();
	}

	private Stream<Ticket> fetch_(Query<Ticket, Void> query) {
		return load(query).stream();
	}
	
	private Page<Ticket> load(Query<Ticket, Void> query){
		final Pageable pageable = VaadinUtils.toPageable(query);
		final UserDetails user = securityService.getAuthenticatedUser();
		final Set<Long> departmentIds = resolve(user, LlmsConstants.PREFIX_DEPARTMENT);
		return repository.findByDepartmentIdIn(departmentIds, pageable);
	}
	
	private Set<Long> resolve(UserDetails user, String prefix){
		return user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.filter(text -> text.startsWith(prefix))
				.map(text -> text.substring(prefix.length()))
				.map(Long::parseLong)
				.collect(Collectors.toSet());
	}
}
