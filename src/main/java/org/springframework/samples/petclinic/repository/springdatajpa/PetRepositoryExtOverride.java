package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.samples.petclinic.model.Pet;

public interface PetRepositoryExtOverride {
	
	public void delete(Pet pet);

}
