package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetRepositoryExt;

@Qualifier("PetRepositoryExt")
public interface SpringDataPetRepositoryExt extends PetRepositoryExt, Repository<Pet, Integer> {
	
    @Override
    @Query("SELECT ptype FROM PetType ptype ORDER BY ptype.name")
    List<PetType> findPetTypes() throws DataAccessException;

}
