package org.springframework.samples.petclinic.course.dto;

import lombok.Getter;

@Getter()
public class CreateCourseRequest {
	private String name;
	private String description;
	private String difficulty;
	private Integer vetId;

	public CreateCourseRequest() {
	}

	public CreateCourseRequest(String name, String description, String difficulty, Integer vetId) {
		this.name = name;
		this.description = description;
		this.difficulty = difficulty;
		this.vetId = vetId;
	}

	@Override
	public String toString() {
		return "CreateCourseRequest{" +
			"name='" + name + '\'' +
			", description='" + description + '\'' +
			", difficulty='" + difficulty + '\'' +
			", vetId=" + vetId +
			'}';
	}
}
