package com.revosystems.llms.user;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private UserDetailsManager userDetailsManager;
	
	private UserDto toDto(UserDetails userDetails) {
		final UserDto dto = new UserDto();
		dto.setUsername(userDetails.getUsername());
		dto.setPassword(null);
		dto.setAuthorities(userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet()));
		return dto;
	}
	
	private UserDetails toUserDetails(UserDto dto) {
		final Set<GrantedAuthority> authorities = dto.getAuthorities().stream()
			.distinct()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toSet());
		return new User(dto.getUsername(), passwordEncoder.encode(dto.getPassword()), authorities);
	}
	
	@GetMapping("/me")
	public UserDto whoAmI(@AuthenticationPrincipal UserDetails userDetails) {
		return toDto(userDetails);
	}
	
	@PostMapping
	public UserDto create(@RequestBody UserDto userDto) {
		final UserDetails userDetails = toUserDetails(userDto);
		userDetailsManager.createUser(userDetails);
		final UserDetails persistedUserDetails = userDetailsManager.loadUserByUsername(userDetails.getUsername());
		return toDto(persistedUserDetails);
	}
	
	@PutMapping
	public UserDto update(@RequestBody UserDto userDto) {
		final UserDetails userDetails = toUserDetails(userDto);
		userDetailsManager.createUser(userDetails);
		final UserDetails persistedUserDetails = userDetailsManager.loadUserByUsername(userDetails.getUsername());
		return toDto(persistedUserDetails);
	}
	
	@DeleteMapping("/{username}")
	public void deleteByUsername(@PathVariable @NotBlank String username) {
		userDetailsManager.deleteUser(username);
	}
	
	@PostMapping(params = {"oldPassword", "newPassword"}, 
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void changePassword(@NotBlank String oldPassword,@NotBlank String newPassword) {
		userDetailsManager.changePassword(
				passwordEncoder.encode(oldPassword), 
				passwordEncoder.encode(newPassword));
	}
	
	@GetMapping("/verify/{username}")
	public boolean exists(@PathVariable String username) {
		return userDetailsManager.userExists(username);
	}
	
	@GetMapping("/{username}")
	public UserDto findByUsername(@PathVariable String username){
		final UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
		return toDto(userDetails);
	}
}
