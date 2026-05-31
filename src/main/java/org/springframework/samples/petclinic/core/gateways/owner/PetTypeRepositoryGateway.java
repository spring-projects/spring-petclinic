package org.springframework.samples.petclinic.core.gateways.owner;

import java.util.List;
import java.util.Optional;

import org.springframework.samples.petclinic.core.domain.owner.PetType;

public interface PetTypeRepositoryGateway {

	List<PetType> findPetTypes();

	Optional<PetType> findByName(String name);

}
