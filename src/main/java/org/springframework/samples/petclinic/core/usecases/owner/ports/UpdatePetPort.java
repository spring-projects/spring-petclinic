package org.springframework.samples.petclinic.core.usecases.owner.ports;

import org.springframework.samples.petclinic.core.domain.owner.Pet;

public interface UpdatePetPort {

	void updatePet(int ownerId, int petId, Pet pet);

}
