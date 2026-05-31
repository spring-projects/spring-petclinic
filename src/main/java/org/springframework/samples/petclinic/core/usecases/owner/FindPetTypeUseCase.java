package org.springframework.samples.petclinic.core.usecases.owner;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.samples.petclinic.core.domain.owner.PetType;
import org.springframework.samples.petclinic.core.gateways.owner.PetTypeRepositoryGateway;
import org.springframework.samples.petclinic.core.usecases.owner.ports.FindPetTypePort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindPetTypeUseCase implements FindPetTypePort {

	private final PetTypeRepositoryGateway petTypeRepositoryGateway;

	@Override
	public List<PetType> findAll() {
		return petTypeRepositoryGateway.findPetTypes();
	}

	@Override
	public Optional<PetType> findByName(String name) {
		return petTypeRepositoryGateway.findByName(name);
	}

}
