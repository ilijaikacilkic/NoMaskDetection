package com.levi9.alertingservice.model;

import org.mapstruct.Mapper;

import com.levi9.alertingservice.rabbitmq.AlertMessage;

@Mapper(componentModel = "spring")
public interface MessageMapper {

	public Message alertMessageToMessage(AlertMessage alertMessage);
}
