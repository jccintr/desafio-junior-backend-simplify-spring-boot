package com.jcsoftware.todoapi.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jcsoftware.todoapi.controllers.exceptions.StandardError;
import com.jcsoftware.todoapi.controllers.exceptions.ValidationError;
import com.jcsoftware.todoapi.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
	
	
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e,HttpServletRequest request){
		
		String error = "Resource not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(),status.value(),error,e.getMessage(),request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> methodArgumentNotValid(MethodArgumentNotValidException e,HttpServletRequest request){
		String error = "Invalid Argument";
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationError err = new ValidationError(Instant.now(),status.value(),error,"Invalid parameters",request.getRequestURI());
		for(FieldError f : e.getBindingResult().getFieldErrors()) {
			err.AddError(f.getField(), f.getDefaultMessage());
		}
		return ResponseEntity.status(status).body(err);
	}
	
	
	
}