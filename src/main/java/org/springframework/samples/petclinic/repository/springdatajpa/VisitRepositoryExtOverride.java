package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.samples.petclinic.model.Visit;

public interface VisitRepositoryExtOverride {
	
	public void delete(Visit visit);

}
