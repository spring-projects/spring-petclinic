package org.springframework.samples.petclinic.owner;

public interface PetAttributesService {

	PetAttributes save(PetAttributes attributes);

	PetAttributes findByPetId(int petId);

}