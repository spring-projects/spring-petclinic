package org.springframework.samples.petclinic.adapters.web.owner.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetTypeDto {

	private Integer id;

	private String name;

	@Override
	public String toString() {
		return name != null ? name : "<null>";
	}

}
