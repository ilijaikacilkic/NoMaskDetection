package com.levi9.alertingservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Getter
@Setter
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	@Column(name="timestamp")
	private Date timestamp;
	@Column(name="image_data", columnDefinition = "MEDIUMBLOB")
	private byte[] imageData;
	@Column(name="uploader")
	private String uploader;
}