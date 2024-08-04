package com.levi9.alertingservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.levi9.alertingservice.model.Message;
import com.levi9.alertingservice.service.MessageService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(value = "/message")
@RequiredArgsConstructor
public class MessageController {
	
	private final MessageService service;
	
	@CrossOrigin("*")
	@GetMapping(value = "")
	@PreAuthorize("hasAuthority('READ_IMAGE')")
    public ResponseEntity<List<Message>> findAllMessages() {
        return ResponseEntity.ok(service.getAllMessages());
    }
	
	@CrossOrigin("*")
	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('READ_IMAGE')")
    public ResponseEntity<?> findMessageById(@PathVariable Long id) {
        Message message = service.getMessageById(id);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
	
	@CrossOrigin("*")
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('DELETE_IMAGE')")
	public ResponseEntity<?> deleteMessageById(@PathVariable Long id) {
		service.deleteMessageById(id);
		
		return new ResponseEntity<>("Message deleted.", HttpStatus.OK);
	}
	
}
