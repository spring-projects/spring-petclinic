package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;

public interface OwnerRepositoryExt extends OwnerRepository {
	
	Collection<Owner> findAll() throws DataAccessException;

	void delete(Owner owner) throws DataAccessException;


}
