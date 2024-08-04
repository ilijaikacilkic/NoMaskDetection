package com.levi9.alertingservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.levi9.alertingservice.security.AuthenticationRequest;

@FeignClient(value="authentication-client", url="${constants.authService}")
public interface AuthenticationClient {
	
	@PostMapping(value="/auth/login")
	ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest);
}
