package org.springframework.samples.petclinic.adapters.mappers.owner;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.samples.petclinic.adapters.dtos.owner.OwnerDto;
import org.springframework.samples.petclinic.adapters.dtos.owner.PetDto;
import org.springframework.samples.petclinic.adapters.dtos.owner.PetTypeDto;
import org.springframework.samples.petclinic.adapters.dtos.owner.VisitDto;
import org.springframework.samples.petclinic.core.domain.owner.Owner;
import org.springframework.samples.petclinic.core.domain.owner.Pet;
import org.springframework.samples.petclinic.core.domain.owner.PetType;
import org.springframework.samples.petclinic.core.domain.owner.Visit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OwnerMapper {

	public static OwnerDto toDto(Owner owner) {
		OwnerDto dto = new OwnerDto();
		dto.setId(owner.getId());
		dto.setFirstName(owner.getFirstName());
		dto.setLastName(owner.getLastName());
		dto.setAddress(owner.getAddress());
		dto.setCity(owner.getCity());
		dto.setTelephone(owner.getTelephone());
		owner.getPets().forEach(p -> dto.getPets().add(petToDto(p)));
		return dto;
	}

	public static PetDto petToDto(Pet pet) {
		PetDto dto = new PetDto();
		dto.setId(pet.getId());
		dto.setName(pet.getName());
		dto.setBirthDate(pet.getBirthDate());
		if (pet.getType() != null) {
			dto.setType(petTypeToDto(pet.getType()));
		}
		pet.getVisits().forEach(v -> dto.addVisit(visitToDto(v)));
		return dto;
	}

	public static PetTypeDto petTypeToDto(PetType petType) {
		PetTypeDto dto = new PetTypeDto();
		dto.setId(petType.getId());
		dto.setName(petType.getName());
		return dto;
	}

	public static VisitDto visitToDto(Visit visit) {
		VisitDto dto = new VisitDto();
		dto.setId(visit.getId());
		dto.setDate(visit.getDate());
		dto.setDescription(visit.getDescription());
		return dto;
	}

	public static Owner toDomain(OwnerDto dto) {
		Owner owner = new Owner();
		owner.setId(dto.getId());
		owner.setFirstName(dto.getFirstName());
		owner.setLastName(dto.getLastName());
		owner.setAddress(dto.getAddress());
		owner.setCity(dto.getCity());
		owner.setTelephone(dto.getTelephone());
		return owner;
	}

	public static Visit visitToDomain(VisitDto dto) {
		Visit visit = new Visit();
		visit.setId(dto.getId());
		visit.setDate(dto.getDate());
		visit.setDescription(dto.getDescription());
		return visit;
	}

	public static PetType petTypeToDomain(PetTypeDto dto) {
		PetType pt = new PetType();
		pt.setId(dto.getId());
		pt.setName(dto.getName());
		return pt;
	}

	public static Pet petToDomain(PetDto dto) {
		Pet pet = new Pet();
		pet.setId(dto.getId());
		pet.setName(dto.getName());
		pet.setBirthDate(dto.getBirthDate());
		if (dto.getType() != null) {
			pet.setType(petTypeToDomain(dto.getType()));
		}
		return pet;
	}

}
