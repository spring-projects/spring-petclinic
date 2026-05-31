package org.springframework.samples.petclinic.adapters.repositories.owner;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.samples.petclinic.adapters.entities.owner.PetTypeJpaEntity;
import org.springframework.samples.petclinic.core.domain.owner.PetType;
import org.springframework.samples.petclinic.core.gateways.owner.PetTypeRepositoryGateway;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class PetTypeRepositoryAdapter implements PetTypeRepositoryGateway {

	private final PetTypeSpringDataRepository springDataRepository;

	@Override
	public List<PetType> findPetTypes() {
		return springDataRepository.findAllOrderedByName().stream().map(this::toDomain).toList();
	}

	@Override
	public Optional<PetType> findByName(String name) {
		return springDataRepository.findByName(name).map(this::toDomain);
	}

	private PetType toDomain(PetTypeJpaEntity e) {
		PetType pt = new PetType();
		pt.setId(e.getId());
		pt.setName(e.getName());
		return pt;
	}

}
