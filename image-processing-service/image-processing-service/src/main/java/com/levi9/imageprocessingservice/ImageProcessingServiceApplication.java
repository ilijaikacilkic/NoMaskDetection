package com.levi9.imageprocessingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ImageProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageProcessingServiceApplication.class, args);
	}

}
