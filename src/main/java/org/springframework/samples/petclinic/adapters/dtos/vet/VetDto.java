package org.springframework.samples.petclinic.adapters.dtos.vet;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VetDto {

	private final String firstName;

	private final String lastName;

	private final List<SpecialtyDto> specialties;

}
