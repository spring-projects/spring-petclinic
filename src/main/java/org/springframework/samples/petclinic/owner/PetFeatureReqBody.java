package org.springframework.samples.petclinic.owner;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetFeatureReqBody {

	@NotNull(message = "Pet id cannot be null")
	private Integer petId;
	private String skinColor, skinType, temperament;

	private Integer height;
	private Double weight;

}
