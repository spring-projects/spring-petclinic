package org.springframework.samples.petclinic.vaccine.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VaccineRequest {

	@NotBlank(message = "Vaccine name is required")
	private String vaccineName;

}
