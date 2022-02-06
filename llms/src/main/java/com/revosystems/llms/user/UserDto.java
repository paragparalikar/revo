package com.revosystems.llms.user;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserDto {

	@NotBlank private String username;
	@NotBlank private String password;
	@NotEmpty private Set<String> authorities;
	
}
