package com.jcsoftware.todoapi.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcsoftware.todoapi.records.InsertTodoRecord;
import com.jcsoftware.todoapi.records.TodoRecord;
import com.jcsoftware.todoapi.records.UpdateTodoRecord;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TodoControllerIntegrationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Long existingId;
	private Long nonExistingId;
	private Long totalTodos;
	
	
	@BeforeEach
	void setup() throws Exception {
		
		existingId = 1L;
		nonExistingId = 1000L;
		totalTodos = 20L;
		
		
	}
	
	@Test
	public void findAllShouldReturnTodoOrderedList() throws Exception {
		ResultActions result = mockMvc.perform( get("/todos")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$[0].name").value("Automotive"));
		result.andExpect(jsonPath("$[3].name").value("Baby"));
		result.andExpect(jsonPath("$[12].name").value("Kids"));
	}
	
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc.perform( get("/todos/{id}",nonExistingId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnResourceFoundWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform( get("/todos/{id}",existingId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		result.andExpect(jsonPath("$.done").exists());
		
	}
	
	@Test
	public void deleteByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc.perform( delete("/todos/{id}",nonExistingId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteByIdShouldReturnNoContentWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform( delete("/todos/{id}",existingId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void insertShouldReturnCreatedRecordWhenValidData() throws Exception {
		String newName = "New Todo name";
		String newDescription = "New Todo description";
		Integer newPriority = 1;
		InsertTodoRecord insertTodoRecord = new InsertTodoRecord(newName,newDescription,newPriority);
		String requestBody = objectMapper.writeValueAsString(insertTodoRecord);
		ResultActions result = mockMvc.perform( post("/todos")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.id").value(totalTodos+1));
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.name").value(newName));
		result.andExpect(jsonPath("$.description").exists());
		result.andExpect(jsonPath("$.description").value(newDescription));
		result.andExpect(jsonPath("$.priority").exists());
		result.andExpect(jsonPath("$.priority").value(newPriority));
		result.andExpect(jsonPath("$.done").exists());
		result.andExpect(jsonPath("$.done").value(false));
	}
	
	@Test
	public void insertShouldReturn422WhenBlankName() throws Exception {
		String newName = "";
		String newDescription = "New Todo description";
		Integer newPriority = 1;
		InsertTodoRecord insertTodoRecord = new InsertTodoRecord(newName,newDescription,newPriority);
		String requestBody = objectMapper.writeValueAsString(insertTodoRecord);
		ResultActions result = mockMvc.perform( post("/todos")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("name"));
		result.andExpect(jsonPath("$.errors[0].message").value("Campo requerido"));
	}
	
	@Test
	public void insertShouldReturn422WhenBlankDescription() throws Exception {
		String newName = "New Todo name";
		String newDescription = "";
		Integer newPriority = 1;
		InsertTodoRecord insertTodoRecord = new InsertTodoRecord(newName,newDescription,newPriority);
		String requestBody = objectMapper.writeValueAsString(insertTodoRecord);
		ResultActions result = mockMvc.perform( post("/todos")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("description"));
		result.andExpect(jsonPath("$.errors[0].message").value("Campo requerido"));
	}
	
	@Test
	public void insertShouldReturn422WhenInvalidPriority() throws Exception {
		String newName = "New Todo name";
		String newDescription = "New Todo description";
		Integer newPriority = 5;
		InsertTodoRecord insertTodoRecord = new InsertTodoRecord(newName,newDescription,newPriority);
		String requestBody = objectMapper.writeValueAsString(insertTodoRecord);
		ResultActions result = mockMvc.perform( post("/todos")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("priority"));
		result.andExpect(jsonPath("$.errors[0].message").value("A prioridade deve ser entre 1 e 3"));
	}
	
	@Test
	public void updateShouldReturnTodoRecordWhenIdExists() throws Exception {
		
		String newName = "Todo name updated";
		String newDescription = "Todo description updated";
		Integer newPriority = 1;
		Boolean newDone = true;
		
		UpdateTodoRecord updateTodoRecord = new UpdateTodoRecord(newName,newDescription,newPriority,newDone);
		String requestBody = objectMapper.writeValueAsString(updateTodoRecord);
		
		ResultActions result = mockMvc.perform( put("/todos/{id}",existingId)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.name").value(newName));
		result.andExpect(jsonPath("$.description").exists());
		result.andExpect(jsonPath("$.description").value(newDescription));
		result.andExpect(jsonPath("$.priority").exists());
		result.andExpect(jsonPath("$.priority").value(newPriority));
		result.andExpect(jsonPath("$.done").exists());
		result.andExpect(jsonPath("$.done").value(newDone));
		
		
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		
		String newName = "Todo name updated";
		String newDescription = "Todo description updated";
		Integer newPriority = 1;
		Boolean newDone = true;
		
		UpdateTodoRecord updateTodoRecord = new UpdateTodoRecord(newName,newDescription,newPriority,newDone);
		String requestBody = objectMapper.writeValueAsString(updateTodoRecord);
		
		ResultActions result = mockMvc.perform( put("/todos/{id}",nonExistingId)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isNotFound());
	
	}
	
	@Test
	public void updateShouldReturn422WhenBlankNameAndExistingId() throws Exception {
		
		String newName = "";
		String newDescription = "Todo description updated";
		Integer newPriority = 1;
		Boolean newDone = true;
		
		UpdateTodoRecord updateTodoRecord = new UpdateTodoRecord(newName,newDescription,newPriority,newDone);
		String requestBody = objectMapper.writeValueAsString(updateTodoRecord);
		
		ResultActions result = mockMvc.perform( put("/todos/{id}",existingId)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("name"));
		result.andExpect(jsonPath("$.errors[0].message").value("Campo requerido"));
	}
	
	@Test
	public void updateShouldReturn422WhenBlankDescriptionAndExistingId() throws Exception {
		
		String newName = "Todo name updated";
		String newDescription = "";
		Integer newPriority = 1;
		Boolean newDone = true;
		
		UpdateTodoRecord updateTodoRecord = new UpdateTodoRecord(newName,newDescription,newPriority,newDone);
		String requestBody = objectMapper.writeValueAsString(updateTodoRecord);
		
		ResultActions result = mockMvc.perform( put("/todos/{id}",existingId)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("description"));
		result.andExpect(jsonPath("$.errors[0].message").value("Campo requerido"));
	}
	
	@Test
	public void updateShouldReturn422WhenInvalidPriorityAndExistingId() throws Exception {
		
		String newName = "Todo name updated";
		String newDescription = "Todo description updated";
		Integer newPriority = 5;
		Boolean newDone = true;
		
		UpdateTodoRecord updateTodoRecord = new UpdateTodoRecord(newName,newDescription,newPriority,newDone);
		String requestBody = objectMapper.writeValueAsString(updateTodoRecord);
		
		ResultActions result = mockMvc.perform( put("/todos/{id}",existingId)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("priority"));
		result.andExpect(jsonPath("$.errors[0].message").value("A prioridade deve ser entre 1 e 3"));
	}
	

}
