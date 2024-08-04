package com.levi9.alertingservice.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.levi9.alertingservice.model.Message;
import com.levi9.alertingservice.model.MessageMapper;
import com.levi9.alertingservice.rabbitmq.AlertMessage;
import com.levi9.alertingservice.repository.MessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {
	
	private final MessageRepository repository;
	
	private final MessageMapper messageMapper;
	
	public Message saveMessage(AlertMessage alertMessage) {
		Message message = messageMapper.alertMessageToMessage(alertMessage);
		
		return repository.save(message);
	}
	
	public List<Message> getAllMessages() {
		return repository.findAll();
	}
	
	public Message getMessageById(Long id) {
		 if (repository.existsById(id)) {
			 return repository.getById(id);
		 } else {
			 throw new EntityNotFoundException("Message with ID " + id + " not found.");
		 }
	}
	
	public void deleteMessageById(Long id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
		} else {
			throw new EntityNotFoundException("Message with ID " + id + " not found.");
		}
	}
}
