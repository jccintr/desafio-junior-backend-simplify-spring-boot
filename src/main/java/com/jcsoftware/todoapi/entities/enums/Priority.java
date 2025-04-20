package com.jcsoftware.todoapi.entities.enums;

import com.jcsoftware.todoapi.entities.enums.Priority;

public enum Priority {
	LOW(1), MEDIUM(2), HIGH(3);
	
	private int code;
	
	private Priority(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}

	public static Priority valueOf(int code) {

		for (Priority os : Priority.values()) {
			if (os.getCode() == code) {
				return os;
			}
		}
		throw new IllegalArgumentException("Invalid Priority code");
	}
}
