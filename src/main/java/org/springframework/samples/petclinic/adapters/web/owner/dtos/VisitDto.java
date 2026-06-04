package org.springframework.samples.petclinic.adapters.web.owner.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitDto {

	private Integer id;

	private LocalDate date;

	@NotBlank
	private String description;

	public VisitDto() {
		this.date = LocalDate.now();
	}

	public boolean isNew() {
		return this.id == null;
	}

}
