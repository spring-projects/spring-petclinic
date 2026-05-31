package org.springframework.samples.petclinic.core.usecases.owner.ports;

import java.util.List;
import java.util.Optional;

import org.springframework.samples.petclinic.core.domain.owner.PetType;

public interface FindPetTypePort {

	List<PetType> findAll();

	Optional<PetType> findByName(String name);

}
