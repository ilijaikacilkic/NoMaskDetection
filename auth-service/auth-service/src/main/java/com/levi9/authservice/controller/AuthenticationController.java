 package com.levi9.authservice.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.levi9.authservice.model.User;
import com.levi9.authservice.security.AuthenticationRequest;
import com.levi9.authservice.security.AuthenticationResponse;
import com.levi9.authservice.security.JWTUtils;
import com.levi9.authservice.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthenticationController {
	
	private final AuthenticationManager authenticationManager;

	private final UserService userService;
	
	private final JWTUtils jwtUtils;

	@PostMapping(value = "/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException, InvalidKeySpecException, NoSuchAlgorithmException {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		
		final User user = userService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtUtils.generateToken(user);
		
		return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);
	}
}
