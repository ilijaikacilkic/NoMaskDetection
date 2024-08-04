package com.levi9.imageprocessingservice.security;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {

	@NotEmpty(message = "{validation.username.NotEmpty}")
	private String username;
	@NotEmpty(message = "{validation.password.NotEmpty}")
	private String password;
}
