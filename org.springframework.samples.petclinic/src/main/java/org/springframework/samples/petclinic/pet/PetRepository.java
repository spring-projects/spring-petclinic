package org.springframework.samples.petclinic.pet;

public interface PetRepository {
	
	Pet getPet(Long owner, String name);
	
	void savePet(Pet pet);
	
}
