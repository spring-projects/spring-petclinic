package org.springframework.samples.petclinic.core.usecases.owner.ports;

import org.springframework.samples.petclinic.core.domain.owner.Pet;

public interface AddPetPort {

	void addPet(int ownerId, Pet pet);

}
