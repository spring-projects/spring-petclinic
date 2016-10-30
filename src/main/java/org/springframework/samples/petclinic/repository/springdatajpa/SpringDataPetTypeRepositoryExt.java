package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetTypeRepositoryExt;

@Qualifier("PetTypeRepositoryExt")
public interface SpringDataPetTypeRepositoryExt extends PetTypeRepositoryExt, Repository<PetType, Integer> {

}
