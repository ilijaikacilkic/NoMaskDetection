package com.levi9.authservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import com.levi9.authservice.model.Authority;
import com.levi9.authservice.model.User;
import com.levi9.authservice.security.AuthenticationRequest;
import com.levi9.authservice.security.AuthenticationResponse;
import com.levi9.authservice.security.JWTKeyProvider;
import com.levi9.authservice.security.JWTUtils;
import com.levi9.authservice.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;


@RunWith(MockitoJUnitRunner.class)
class AuthenticationControllerTest {
	
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private UserService userService;

	private JWTUtils jwtUtils;
	
	private AuthenticationController controller;
	
	private String privateKey = "-----BEGIN PRIVATE KEY-----\n"
			+ "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\n"
			+ "9HB+mhzYLxqy0a1HBG21HdxKhQ==\n"
			+ "-----END PRIVATE KEY-----";


	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		JWTKeyProvider key = new JWTKeyProvider(privateKey);
		jwtUtils = new JWTUtils(15, key);
		controller = new AuthenticationController(authenticationManager, userService, jwtUtils);
	}

	@Test	
	void successfulLogin() throws BadCredentialsException, InvalidKeySpecException, NoSuchAlgorithmException {
		ResponseEntity res = new ResponseEntity<>(HttpStatus.OK);
		Authority authority = new Authority();
		authority.setId(1L);
		authority.setName("MyRole");
		Collection<Authority> authorities = Arrays.asList(authority);
		User user = new User("admin", "admin", "John", "Doe", authorities);
		Mockito.when(userService.loadUserByUsername("admin")).thenReturn(user);
		
		AuthenticationRequest ar = new AuthenticationRequest();
		ar.setUsername("admin");
		ar.setPassword("admin");
		ResponseEntity<?> response = controller.createAuthenticationToken(ar);
		AuthenticationResponse authenticationResponse = (AuthenticationResponse) response.getBody();
		
		JWTKeyProvider jwtKeyProvider = new JWTKeyProvider(privateKey);
		Jwt jwt = Jwts.parser().setSigningKey(jwtKeyProvider.getPrivateKey()).parseClaimsJws(authenticationResponse.getAccessToken()); 
		Claims claims = (Claims) jwt.getBody();
		List<String> claimAuthorities = (List<String>) claims.get("authorities");
		
		assertEquals(authority.getAuthority(), claimAuthorities.get(0)); 
		Mockito.verify(userService, Mockito.times(1)).loadUserByUsername(ar.getUsername());
		Mockito.verifyNoMoreInteractions(userService);
		Mockito.reset(userService); 
	}	
	
}
