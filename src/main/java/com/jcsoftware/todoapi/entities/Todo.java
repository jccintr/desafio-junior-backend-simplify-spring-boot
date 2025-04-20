package com.jcsoftware.todoapi.entities;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "todos")
public class Todo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String description;
	private Integer priority;
	private Boolean done;

	
	public Todo() {
		
	}


	public Todo(String name, String description, Integer priority) {
		super();
		this.name = name;
		this.description = description;
		this.priority = priority;
		this.done = false;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Integer getPriority() {
		return priority;
	}


	public void setPriority(Integer priority) {
		this.priority = priority;
	}


	public Boolean getDone() {
		return done;
	}


	public void setDone(Boolean done) {
		this.done = done;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Todo other = (Todo) obj;
		return Objects.equals(id, other.id);
	}
	
	
}
