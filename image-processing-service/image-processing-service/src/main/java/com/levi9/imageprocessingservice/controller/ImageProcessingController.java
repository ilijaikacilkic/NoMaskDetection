 package com.levi9.imageprocessingservice.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.levi9.imageprocessingservice.exception.InvalidImageDimensionsException;
import com.levi9.imageprocessingservice.exception.NoFileSubmittedException;
import com.levi9.imageprocessingservice.exception.UnsupportedFileTypeException;
import com.levi9.imageprocessingservice.security.JWTUtils;
import com.levi9.imageprocessingservice.service.ImageProcessingService;

import lombok.RequiredArgsConstructor;

 @RestController
 @RequestMapping(value = "/image")
 @RequiredArgsConstructor
 public class ImageProcessingController {

 	private final ImageProcessingService service;

 	private final JWTUtils jwtUtils;
	
	@CrossOrigin("*")
	@PreAuthorize("hasAuthority('UPLOAD_IMAGE')")
	@PostMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException, UnsupportedFileTypeException, InvalidImageDimensionsException, NoFileSubmittedException {
		String uploader = jwtUtils.extractName(jwtUtils.getJwtFromRequest(request));
		service.processImage(file, uploader);
		
		return new ResponseEntity<>("Image submitted successfully.", HttpStatus.OK);
	}
 }
