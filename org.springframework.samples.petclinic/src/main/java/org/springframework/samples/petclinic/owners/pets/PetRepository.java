package org.springframework.samples.petclinic.owners.pets;

public interface PetRepository {
	
	Pet getPet(Long owner, String name);
	
	void savePet(Pet pet);
	
}
