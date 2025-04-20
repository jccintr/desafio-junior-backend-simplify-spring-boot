package com.jcsoftware.todoapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jcsoftware.todoapi.entities.Todo;
import com.jcsoftware.todoapi.records.InsertTodoRecord;
import com.jcsoftware.todoapi.records.TodoRecord;
import com.jcsoftware.todoapi.records.UpdateTodoRecord;
import com.jcsoftware.todoapi.repositories.TodoRepository;
import com.jcsoftware.todoapi.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TodoService {
	
	@Autowired
	private TodoRepository repository;

	public List<TodoRecord> findAll() {
		
		List<Todo> todos = repository.findAll();
		return todos.stream().map(x-> new TodoRecord(x)).toList();
		
	}

	@Transactional
	public TodoRecord insert(InsertTodoRecord dto) {
		Todo newTodo = new Todo();
		newTodo.setName(dto.name());
		newTodo.setDescription(dto.description());
		newTodo.setDone(false);
		newTodo.setPriority(dto.priority());
		newTodo = repository.save(newTodo);
		return new TodoRecord(newTodo);
		
	}
	
	@Transactional(readOnly = true)
	public TodoRecord findById(Long id) {
		
		Optional<Todo> todoO = repository.findById(id);
		Todo todo = todoO.orElseThrow(() -> new ResourceNotFoundException(id));
		
		TodoRecord todoRecord = new TodoRecord(todo);
		
		return todoRecord;
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public void delete(Long id) {
		
		if (repository.existsById(id)) {
			repository.deleteById(id);
		} else {
			throw (new ResourceNotFoundException(id));
		}
		
	}
	
	@Transactional
	public TodoRecord update(Long id, UpdateTodoRecord dto) {
		
		try {
			Todo todo = repository.getReferenceById(id);
			todo.setName(dto.name());
			todo.setDescription(dto.description());
			todo.setDone(dto.done());
			todo.setPriority(dto.priority());
			todo = repository.save(todo);
			return new TodoRecord(todo);
		} catch (EntityNotFoundException e) {
			throw (new ResourceNotFoundException(id));
		}

	}

}
