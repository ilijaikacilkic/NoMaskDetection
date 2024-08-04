package com.levi9.alertingservice.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.levi9.alertingservice.service.MessagePublisher;
import com.levi9.alertingservice.service.MessageService;
import com.levi9.alertingservice.model.Message;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AlertMessageListener {	
	
	private final MessageService messageService;
	
	private final MessagePublisher messagePublisher;
	
	@RabbitListener(queues = AlertMQConfig.QUEUE_NAME)
	public void listen(AlertMessage alertMessage) {
		Message message = messageService.saveMessage(alertMessage);
		messagePublisher.publish(message);		
	}
	
}
