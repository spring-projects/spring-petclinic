package org.springframework.samples.petclinic.core.usecases.owner;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;

import org.springframework.samples.petclinic.core.domain.owner.Owner;
import org.springframework.samples.petclinic.core.domain.owner.Pet;
import org.springframework.samples.petclinic.core.gateways.owner.OwnerRepositoryGateway;
import org.springframework.samples.petclinic.core.usecases.owner.ports.AddPetPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddPetUseCase implements AddPetPort {

	private final OwnerRepositoryGateway ownerRepositoryGateway;

	@Override
	public void addPet(int ownerId, Pet pet) {
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("Pet birth date cannot be in the future.");
		}
		Owner owner = ownerRepositoryGateway.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId));
		if (owner.getPet(pet.getName(), true) != null) {
			throw new IllegalArgumentException("Pet with name '" + pet.getName() + "' already exists for this owner.");
		}
		owner.addPet(pet);
		ownerRepositoryGateway.save(owner);
	}

}
