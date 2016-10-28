package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Visit;

public interface VisitRepositoryExt extends VisitRepository {
	
	Visit findById(int id) throws DataAccessException;
	
	Collection<Visit> findAll() throws DataAccessException;

	void delete(Visit visit) throws DataAccessException;

}
