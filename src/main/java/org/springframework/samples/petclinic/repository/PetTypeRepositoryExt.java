package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.PetType;

public interface PetTypeRepositoryExt {
	
	PetType findById(int id);
	
	Collection<PetType> findAll() throws DataAccessException;

	void save(PetType petType) throws DataAccessException;
	
	void delete(PetType petType) throws DataAccessException;

}
