package com.jcsoftware.todoapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcsoftware.todoapi.repositories.TodoRepository;

@Service
public class TodoService {
	
	@Autowired
	private TodoRepository repository;

}
