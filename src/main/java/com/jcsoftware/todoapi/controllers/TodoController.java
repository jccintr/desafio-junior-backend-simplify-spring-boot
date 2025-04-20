package com.jcsoftware.todoapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.jcsoftware.todoapi.services.TodoService;

@RestController
public class TodoController {
	
	@Autowired
	private TodoService service;

}
