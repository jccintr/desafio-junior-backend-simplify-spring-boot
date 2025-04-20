package com.jcsoftware.todoapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jcsoftware.todoapi.entities.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Long> {

}
