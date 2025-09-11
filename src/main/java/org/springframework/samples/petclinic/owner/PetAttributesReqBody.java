package org.springframework.samples.petclinic.owner;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetAttributesReqBody {

//	@NotNull(message = "Pet id cannot be null")
//	private Integer petId;
	private String temperament;

	private Integer height;
	private Double weight;

}
