package org.springframework.samples.petclinic.owner;

import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record VisitDto(
	Integer id,
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate visitDate,
	@NotBlank
	String visitDescription
) {
	public boolean isNew() {
		return id == null;
	}
}

