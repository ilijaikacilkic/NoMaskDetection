package com.levi9.authservice.security;

import lombok.Getter;

@Getter
public class AuthenticationResponse {

	private String accessToken;

	public AuthenticationResponse(String accessToken) {
		this.accessToken = accessToken;
	}

}
