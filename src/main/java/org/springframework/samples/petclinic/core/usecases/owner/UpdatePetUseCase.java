package org.springframework.samples.petclinic.core.usecases.owner;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;

import org.springframework.samples.petclinic.core.domain.owner.Owner;
import org.springframework.samples.petclinic.core.domain.owner.Pet;
import org.springframework.samples.petclinic.core.gateways.owner.OwnerRepositoryGateway;
import org.springframework.samples.petclinic.core.usecases.owner.ports.UpdatePetPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatePetUseCase implements UpdatePetPort {

	private final OwnerRepositoryGateway ownerRepositoryGateway;

	@Override
	public void updatePet(int ownerId, int petId, Pet pet) {
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("Pet birth date cannot be in the future.");
		}
		Owner owner = ownerRepositoryGateway.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId));
		Pet existing = owner.getPet(petId);
		if (existing == null) {
			throw new IllegalArgumentException("Pet not found with id: " + petId);
		}
		if (pet.getName() != null) {
			Pet duplicate = owner.getPet(pet.getName(), false);
			if (duplicate != null && !duplicate.getId().equals(petId)) {
				throw new IllegalArgumentException("Pet with name '" + pet.getName() + "' already exists.");
			}
			existing.setName(pet.getName());
		}
		existing.setBirthDate(pet.getBirthDate());
		if (pet.getType() != null) {
			existing.setType(pet.getType());
		}
		ownerRepositoryGateway.save(owner);
	}

}
