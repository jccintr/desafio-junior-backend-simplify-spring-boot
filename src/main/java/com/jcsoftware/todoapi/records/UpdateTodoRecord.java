package com.jcsoftware.todoapi.records;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTodoRecord(
		
		@NotBlank(message = "Campo requerido")
		String name,
		@NotBlank(message = "Campo requerido")
		String description,
		@NotNull(message = "Campo requerido")
		@Min(value = 1,message="A prioridade deve ser entre 1 e 3")
		@Max(value = 3,message="A prioridade deve ser entre 1 e 3")
		Integer priority,
		Boolean done
		) {

}
