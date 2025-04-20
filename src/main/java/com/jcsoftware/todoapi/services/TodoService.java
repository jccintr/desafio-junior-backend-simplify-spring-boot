package com.jcsoftware.todoapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcsoftware.todoapi.entities.Todo;
import com.jcsoftware.todoapi.records.InsertTodoRecord;
import com.jcsoftware.todoapi.records.TodoRecord;
import com.jcsoftware.todoapi.repositories.TodoRepository;

@Service
public class TodoService {
	
	@Autowired
	private TodoRepository repository;

	public List<TodoRecord> findAll() {
		
		List<Todo> todos = repository.findAll();
		return todos.stream().map(x-> new TodoRecord(x)).toList();
		
	}

	public TodoRecord insert(InsertTodoRecord dto) {
		Todo newTodo = new Todo();
		newTodo.setName(dto.name());
		newTodo.setDescription(dto.description());
		newTodo.setDone(false);
		newTodo.setPriority(dto.priority());
		newTodo = repository.save(newTodo);
		return new TodoRecord(newTodo);
		
	}

}
