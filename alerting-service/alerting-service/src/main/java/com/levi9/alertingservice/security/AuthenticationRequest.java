package com.levi9.alertingservice.security;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthenticationRequest {
	
	@NotEmpty(message = "{validation.username.NotEmpty}")
	private String username;
	@NotEmpty(message = "{validation.password.NotEmpty}")
	private String password;

}
