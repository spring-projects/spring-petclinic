package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Specialty;

public interface SpecialtyRepositoryExt {
	
	Specialty findById(int id);
	
	Collection<Specialty> findAll() throws DataAccessException;
	
	void save(Specialty specialty) throws DataAccessException;
	
	void delete(Specialty specialty) throws DataAccessException;

}
