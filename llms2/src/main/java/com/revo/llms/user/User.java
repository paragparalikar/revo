package com.revo.llms.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.revo.llms.LlmsConstants;
import com.revo.llms.department.Department;
import com.revo.llms.product.Product;

import lombok.Data;

@Data
@Entity
public class User implements UserDetails {
	private static final long serialVersionUID = -8229302054396449750L;

	@Id
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@ElementCollection
	private Set<String> pages = new HashSet<>();

	@ManyToMany
	private Set<Product> products = new HashSet<>();
	
	@ManyToMany
	private Set<Department> departments = new HashSet<>();

	@Transient
	private Set<SimpleGrantedAuthority> authorities = new HashSet<>();
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(authorities.isEmpty()) {
			pages.stream()
				.map(page -> LlmsConstants.PREFIX_PAGE + page)
				.map(SimpleGrantedAuthority::new)
				.forEach(authorities::add);
			departments.stream()
				.map(Department::getId)
				.map(id -> LlmsConstants.PREFIX_DEPARTMENT + id)
				.map(SimpleGrantedAuthority::new)
				.forEach(authorities::add);
			products.stream()
				.map(Product::getId)
				.map(id -> LlmsConstants.PREFIX_PRODUCT + id)
				.map(SimpleGrantedAuthority::new)
				.forEach(authorities::add);
		}
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
