package com.revosystems.llms;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtils {

	public boolean isAdmin(User user) {
		return user.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.anyMatch(Predicate.isEqual("ROLE_ADMIN"));
	}
	
	public boolean hasAuthority(User user, String authority) {
		return isAdmin(user) || user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(Predicate.isEqual(authority));
	}
	
	public Set<Department> getAccessibleDepartments(User user){
		if(isAdmin(user)) return new HashSet<>(Arrays.asList(Department.values()));
		final Set<String> authorities = user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
		return Arrays.stream(Department.values())
				.filter(department -> authorities.contains(department.name()))
				.collect(Collectors.toSet());
	}
}
