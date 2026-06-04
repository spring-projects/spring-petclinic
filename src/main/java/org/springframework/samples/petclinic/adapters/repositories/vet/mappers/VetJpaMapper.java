package org.springframework.samples.petclinic.adapters.repositories.vet.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.samples.petclinic.adapters.entities.vet.SpecialtyEntity;
import org.springframework.samples.petclinic.adapters.entities.vet.VetEntity;
import org.springframework.samples.petclinic.core.domain.vet.Specialty;
import org.springframework.samples.petclinic.core.domain.vet.Vet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VetJpaMapper {

	public static Vet toDomain(VetEntity entity) {
		Vet vet = new Vet();
		vet.setId(entity.getId());
		vet.setFirstName(entity.getFirstName());
		vet.setLastName(entity.getLastName());
		entity.getSpecialties().forEach(s -> vet.addSpecialty(specialtyToDomain(s)));
		return vet;
	}

	public static Specialty specialtyToDomain(SpecialtyEntity entity) {
		Specialty specialty = new Specialty();
		specialty.setId(entity.getId());
		specialty.setName(entity.getName());
		return specialty;
	}

}
