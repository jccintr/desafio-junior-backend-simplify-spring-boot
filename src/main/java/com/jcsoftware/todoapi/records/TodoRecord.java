package com.jcsoftware.todoapi.records;

import com.jcsoftware.todoapi.entities.Todo;

public record TodoRecord(Long id,String name,String description,Boolean done,Integer priority) {
	
	public TodoRecord(Todo entity) {
		
		this(entity.getId(),entity.getName(),entity.getDescription(),entity.getDone(),entity.getPriority());
		
	}

}
