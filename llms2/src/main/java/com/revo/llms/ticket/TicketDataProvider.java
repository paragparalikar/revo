package com.revo.llms.ticket;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.revo.llms.LlmsConstants;
import com.revo.llms.common.security.SecurityService;
import com.revo.llms.util.VaadinUtils;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TicketDataProvider extends AbstractBackEndDataProvider<Ticket, Void> {
	private static final long serialVersionUID = -6035690994345301935L;

	@NonNull private final TicketService ticketService;
	@NonNull private final SecurityService securityService;
	@NonNull private final Supplier<LocalDate> fromDateProvider;
	@NonNull private final Supplier<LocalDate> toDateProvider;
	
	@Override
	protected Stream<Ticket> fetchFromBackEnd(Query<Ticket, Void> query) {
		return load(query).stream();
	}
	
	@Override
	protected int sizeInBackEnd(Query<Ticket, Void> query) {
		return (int) load(query).getTotalElements();
	}
	
	private Page<Ticket> load(Query<Ticket, Void> query){
		final Pageable pageable = VaadinUtils.toPageable(query);
		final UserDetails user = securityService.getAuthenticatedUser();
		final Set<Long> departmentIds = resolve(user, LlmsConstants.PREFIX_DEPARTMENT);
		final Date to = Date.from(toDateProvider.get().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		final Date from = Date.from(fromDateProvider.get().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		return ticketService.findByDepartmentIdInAndOpenTimestampBetween(departmentIds, from, to, pageable);
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
