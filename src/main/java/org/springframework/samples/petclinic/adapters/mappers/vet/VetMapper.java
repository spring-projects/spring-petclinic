package org.springframework.samples.petclinic.adapters.mappers.vet;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.samples.petclinic.adapters.dtos.vet.SpecialtyDto;
import org.springframework.samples.petclinic.adapters.dtos.vet.VetDto;
import org.springframework.samples.petclinic.core.domain.vet.Specialty;
import org.springframework.samples.petclinic.core.domain.vet.Vet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VetMapper {

	public static VetDto toDto(Vet vet) {
		List<SpecialtyDto> specialties = vet.getSpecialties().stream().map(VetMapper::toDto).toList();
		return new VetDto(vet.getFirstName(), vet.getLastName(), specialties);
	}

	public static SpecialtyDto toDto(Specialty specialty) {
		return new SpecialtyDto(specialty.getName());
	}

}
