package org.springframework.samples.petclinic.adapters.repositories.vet;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.petclinic.adapters.entities.vet.SpecialtyEntity;
import org.springframework.samples.petclinic.adapters.entities.vet.VetEntity;
import org.springframework.samples.petclinic.core.domain.vet.Specialty;
import org.springframework.samples.petclinic.core.domain.vet.Vet;
import org.springframework.samples.petclinic.core.gateways.vet.VetRepositoryGateway;
import org.springframework.samples.petclinic.core.usecases.vet.PagedResult;
import org.springframework.stereotype.Repository;

@Repository
class VetRepositoryAdapter implements VetRepositoryGateway {

	private final VetSpringDataRepository springDataRepository;

	VetRepositoryAdapter(VetSpringDataRepository springDataRepository) {
		this.springDataRepository = springDataRepository;
	}

	@Override
	public Collection<Vet> findAll() {
		return springDataRepository.findAll().stream().map(this::toDomain).toList();
	}

	@Override
	public PagedResult<Vet> findAll(int page, int pageSize) {
		Page<VetEntity> jpaPage = springDataRepository.findAll(PageRequest.of(page, pageSize));
		List<Vet> vets = jpaPage.getContent().stream().map(this::toDomain).toList();
		return new PagedResult<>(vets, jpaPage.getTotalElements(), jpaPage.getTotalPages(), page + 1);
	}

	private Vet toDomain(VetEntity entity) {
		Vet vet = new Vet();
		vet.setId(entity.getId());
		vet.setFirstName(entity.getFirstName());
		vet.setLastName(entity.getLastName());
		entity.getSpecialties().forEach(s -> vet.addSpecialty(toSpecialtyDomain(s)));
		return vet;
	}

	private Specialty toSpecialtyDomain(SpecialtyEntity entity) {
		Specialty specialty = new Specialty();
		specialty.setId(entity.getId());
		specialty.setName(entity.getName());
		return specialty;
	}

}
