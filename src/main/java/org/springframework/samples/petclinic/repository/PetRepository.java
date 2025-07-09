package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.ExtendedPetType;

public interface PetRepository extends CrudRepository<ExtendedPetType, Integer> {

}
