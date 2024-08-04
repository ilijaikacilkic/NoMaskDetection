package com.levi9.imageprocessingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import feign.FeignException;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = InvalidImageDimensionsException.class)
	public ResponseEntity<Object> handleInvalidImageSizeException(InvalidImageDimensionsException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = UnsupportedFileTypeException.class)
	public ResponseEntity<Object> handleUnsupportedFileTypeException(UnsupportedFileTypeException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = NoFileSubmittedException.class)
	public ResponseEntity<Object> handleNoFileSubmittedException(NoFileSubmittedException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}	
	
	@ExceptionHandler(value = FeignException.class)
	public ResponseEntity<Object> handleFeignException(FeignException exception) {
		return new ResponseEntity<>(HttpStatus.valueOf(exception.status()));
	}
}
