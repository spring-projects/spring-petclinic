package org.springframework.samples.petclinic.adapters.repositories.owner;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.petclinic.adapters.entities.owner.OwnerJpaEntity;
import org.springframework.samples.petclinic.adapters.repositories.owner.mappers.OwnerJpaMapper;
import org.springframework.samples.petclinic.core.domain.owner.Owner;
import org.springframework.samples.petclinic.core.gateways.owner.OwnerRepositoryGateway;
import org.springframework.samples.petclinic.core.usecases.owner.PagedResult;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class OwnerRepositoryAdapter implements OwnerRepositoryGateway {

	private final OwnerSpringDataRepository springDataRepository;

	@Override
	public Optional<Owner> findById(Integer id) {
		return springDataRepository.findById(id).map(OwnerJpaMapper::toDomain);
	}

	@Override
	public PagedResult<Owner> findByLastNameStartingWith(String lastName, int page, int pageSize) {
		Page<OwnerJpaEntity> jpaPage = springDataRepository.findByLastNameStartingWith(lastName,
				PageRequest.of(page, pageSize));
		List<Owner> owners = jpaPage.getContent().stream().map(OwnerJpaMapper::toDomain).toList();
		return new PagedResult<>(owners, jpaPage.getTotalElements(), jpaPage.getTotalPages(), page + 1);
	}

	@Override
	public Owner save(Owner owner) {
		OwnerJpaEntity saved = springDataRepository.save(OwnerJpaMapper.toJpa(owner));
		return OwnerJpaMapper.toDomain(saved);
	}

}
