package org.springframework.samples.petclinic.vaccine.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Valid
public class VaccinationRequest {

	@NotBlank
	private Integer vaccineId;

	@NotNull(message = "Vaccination date must be provided")
	private LocalDate vaccinationDate;

	@NotBlank
	@Size(min = 10, message = "Description should be at least 10 characters long")
	private String description;

	private boolean isInjected = false;

}
