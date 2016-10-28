package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;

public interface PetRepositoryExt extends PetRepository {
	
	Collection<Pet> findAll() throws DataAccessException;

	void delete(Pet pet) throws DataAccessException;

}
