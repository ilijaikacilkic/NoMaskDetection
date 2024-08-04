package com.levi9.imageprocessingservice.rabbitMQ;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlertMessage {
	@NotNull(message = "{validation.timestamp.NotNull}")
	private Date timestamp;
	@NotNull(message = "{validation.imageData.NotNull}")
	private byte[] imageData;
	@NotEmpty(message = "{validation.uploader.NotEmpty}")
	private String uploader;
}
