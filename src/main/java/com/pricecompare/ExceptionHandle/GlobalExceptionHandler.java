package com.pricecompare.ExceptionHandle;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<ExceptionResponse> handleAnyException(Exception exception) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(ZonedDateTime.now(), false, exception.getMessage(),
				null);
		return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = { NullPointerException.class })
	public ResponseEntity<ExceptionResponse> handleNullPointerException(NullPointerException exception) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(ZonedDateTime.now(), false, exception.getMessage(),
				null);
		return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.NOT_FOUND);
	}

}
