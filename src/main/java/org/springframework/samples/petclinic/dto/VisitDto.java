package org.springframework.samples.petclinic.dto;

import java.time.LocalDate;

public class VisitDto {

	private Long id;

	private LocalDate date;

	private String description;

	private Integer petId;

	public VisitDto() {
	}

	public VisitDto(Long id, LocalDate date, String description, Integer petId) {
		this.id = id;
		this.date = date;
		this.description = description;
		this.petId = petId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPetId() {
		return petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

}
