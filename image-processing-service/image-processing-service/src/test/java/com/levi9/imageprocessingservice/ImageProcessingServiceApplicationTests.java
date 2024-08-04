package com.levi9.imageprocessingservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.levi9.imageprocessingservice.controller.AuthenticationController;
import com.levi9.imageprocessingservice.security.AuthenticationRequest;
import com.levi9.imageprocessingservice.service.AuthenticationClient;



@RunWith(MockitoJUnitRunner.class)
class ImageProcessingServiceApplicationTests {

	@Mock
	private AuthenticationClient authenticationClient;
	private AuthenticationController controller;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		//authenticationClient = mock(AuthenticationClient.class);
		controller = new AuthenticationController(authenticationClient);
	}
	
	@Test
	void contextLoads() {
		AuthenticationRequest ar = new AuthenticationRequest();
		ar.setUsername("admin");
		ar.setPassword("admin");
		ResponseEntity res = ResponseEntity.ok("OK");
		Mockito.when(authenticationClient.login(ar)).thenReturn(res);
		
		ResponseEntity<?> response = controller.createAuthenticationToken(ar);
		
		assertEquals(response, res);
		Mockito.verify(authenticationClient, Mockito.times(1)).login(ar);
		Mockito.verifyNoMoreInteractions(authenticationClient);
		Mockito.reset(authenticationClient); 
		
	}

}
