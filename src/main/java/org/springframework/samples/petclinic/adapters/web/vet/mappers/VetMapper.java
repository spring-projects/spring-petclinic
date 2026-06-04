package org.springframework.samples.petclinic.adapters.web.vet.mappers;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.samples.petclinic.adapters.web.vet.dtos.SpecialtyDto;
import org.springframework.samples.petclinic.adapters.web.vet.dtos.VetDto;
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
