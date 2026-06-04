package org.springframework.samples.petclinic.adapters.web.vet.dtos;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VetDto {

	private final String firstName;

	private final String lastName;

	private final List<SpecialtyDto> specialties;

	public int getNrOfSpecialties() {
		return specialties != null ? specialties.size() : 0;
	}

}
