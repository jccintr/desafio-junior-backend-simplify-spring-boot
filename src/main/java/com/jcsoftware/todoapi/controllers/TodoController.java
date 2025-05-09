package com.jcsoftware.todoapi.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jcsoftware.todoapi.records.InsertTodoRecord;
import com.jcsoftware.todoapi.records.TodoRecord;
import com.jcsoftware.todoapi.records.UpdateTodoRecord;
import com.jcsoftware.todoapi.services.TodoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/todos")
public class TodoController {
	
	@Autowired
	private TodoService service;
	
	@GetMapping
	public ResponseEntity<List<TodoRecord>> findAll(){
		List<TodoRecord> todos = service.findAll();
        return ResponseEntity.ok().body(todos);
	}
	
	@PostMapping
	public ResponseEntity<TodoRecord> insert(@Valid @RequestBody InsertTodoRecord dto){
		
		TodoRecord newTodoRecord = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newTodoRecord.id()).toUri();
		
		return ResponseEntity.created(uri).body(newTodoRecord);
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<TodoRecord> findById(@PathVariable Long id){
		TodoRecord todoRecord = service.findById(id);
		return ResponseEntity.ok().body(todoRecord);
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<TodoRecord> update(@PathVariable Long id,@Valid @RequestBody UpdateTodoRecord dto){
		
		TodoRecord todoRecord = service.update(id, dto);
		return ResponseEntity.ok().body(todoRecord);
		
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}

}
