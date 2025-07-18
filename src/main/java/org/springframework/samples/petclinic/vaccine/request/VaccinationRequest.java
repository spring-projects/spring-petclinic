package org.springframework.samples.petclinic.vaccine.request;

import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
@Valid
public class VaccinationRequest {
	@NotBlank
	private String vaccineName;
	private LocalDate vaccinationDate;
	@NotBlank
	private String description;
}
