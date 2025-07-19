package org.springframework.samples.petclinic.vaccine.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VaccineCreateRequest extends VaccineRequest {

	@NotNull(message = "PetType ID is required")
	private Integer petTypeId;

}
