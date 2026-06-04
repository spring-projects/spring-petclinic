package org.springframework.samples.petclinic.adapters.repositories.vet;

import java.util.Collection;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.petclinic.adapters.entities.vet.VetEntity;
import org.springframework.samples.petclinic.adapters.repositories.vet.mappers.VetJpaMapper;
import org.springframework.samples.petclinic.core.domain.vet.Vet;
import org.springframework.samples.petclinic.core.gateways.vet.VetRepositoryGateway;
import org.springframework.samples.petclinic.core.usecases.vet.PagedResult;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class VetRepositoryAdapter implements VetRepositoryGateway {

	private final VetSpringDataRepository springDataRepository;

	@Override
	public Collection<Vet> findAll() {
		return springDataRepository.findAll().stream().map(VetJpaMapper::toDomain).toList();
	}

	@Override
	public PagedResult<Vet> findAll(int page, int pageSize) {
		Page<VetEntity> jpaPage = springDataRepository.findAll(PageRequest.of(page, pageSize));
		List<Vet> vets = jpaPage.getContent().stream().map(VetJpaMapper::toDomain).toList();
		return new PagedResult<>(vets, jpaPage.getTotalElements(), jpaPage.getTotalPages(), page + 1);
	}

}
