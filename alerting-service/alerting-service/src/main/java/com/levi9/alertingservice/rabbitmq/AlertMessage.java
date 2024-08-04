package com.levi9.alertingservice.rabbitmq;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AlertMessage {

	private Date timestamp;
	private byte[] imageData;
	private String uploader;
}
