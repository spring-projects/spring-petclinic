package org.springframework.samples.petclinic.adapters.repositories.owner;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.petclinic.adapters.entities.owner.OwnerJpaEntity;
import org.springframework.samples.petclinic.adapters.entities.owner.PetJpaEntity;
import org.springframework.samples.petclinic.adapters.entities.owner.PetTypeJpaEntity;
import org.springframework.samples.petclinic.adapters.entities.owner.VisitJpaEntity;
import org.springframework.samples.petclinic.core.domain.owner.Owner;
import org.springframework.samples.petclinic.core.domain.owner.Pet;
import org.springframework.samples.petclinic.core.domain.owner.PetType;
import org.springframework.samples.petclinic.core.domain.owner.Visit;
import org.springframework.samples.petclinic.core.gateways.owner.OwnerRepositoryGateway;
import org.springframework.samples.petclinic.core.usecases.owner.PagedResult;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class OwnerRepositoryAdapter implements OwnerRepositoryGateway {

	private final OwnerSpringDataRepository springDataRepository;

	@Override
	public Optional<Owner> findById(Integer id) {
		return springDataRepository.findById(id).map(this::toDomain);
	}

	@Override
	public PagedResult<Owner> findByLastNameStartingWith(String lastName, int page, int pageSize) {
		Page<OwnerJpaEntity> jpaPage = springDataRepository.findByLastNameStartingWith(lastName,
				PageRequest.of(page, pageSize));
		List<Owner> owners = jpaPage.getContent().stream().map(this::toDomain).toList();
		return new PagedResult<>(owners, jpaPage.getTotalElements(), jpaPage.getTotalPages(), page + 1);
	}

	@Override
	public Owner save(Owner owner) {
		OwnerJpaEntity entity = toJpa(owner);
		OwnerJpaEntity saved = springDataRepository.save(entity);
		return toDomain(saved);
	}

	private Owner toDomain(OwnerJpaEntity e) {
		Owner owner = new Owner();
		owner.setId(e.getId());
		owner.setFirstName(e.getFirstName());
		owner.setLastName(e.getLastName());
		owner.setAddress(e.getAddress());
		owner.setCity(e.getCity());
		owner.setTelephone(e.getTelephone());
		e.getPets().forEach(p -> owner.getPets().add(petToDomain(p)));
		return owner;
	}

	private Pet petToDomain(PetJpaEntity e) {
		Pet pet = new Pet();
		pet.setId(e.getId());
		pet.setName(e.getName());
		pet.setBirthDate(e.getBirthDate());
		if (e.getType() != null) {
			pet.setType(petTypeToDomain(e.getType()));
		}
		e.getVisits().forEach(v -> pet.addVisit(visitToDomain(v)));
		return pet;
	}

	private PetType petTypeToDomain(PetTypeJpaEntity e) {
		PetType pt = new PetType();
		pt.setId(e.getId());
		pt.setName(e.getName());
		return pt;
	}

	private Visit visitToDomain(VisitJpaEntity e) {
		Visit v = new Visit();
		v.setId(e.getId());
		v.setDate(e.getDate());
		v.setDescription(e.getDescription());
		return v;
	}

	private OwnerJpaEntity toJpa(Owner owner) {
		OwnerJpaEntity e = new OwnerJpaEntity();
		e.setId(owner.getId());
		e.setFirstName(owner.getFirstName());
		e.setLastName(owner.getLastName());
		e.setAddress(owner.getAddress());
		e.setCity(owner.getCity());
		e.setTelephone(owner.getTelephone());
		owner.getPets().forEach(p -> e.getPets().add(petToJpa(p)));
		return e;
	}

	private PetJpaEntity petToJpa(Pet pet) {
		PetJpaEntity e = new PetJpaEntity();
		e.setId(pet.getId());
		e.setName(pet.getName());
		e.setBirthDate(pet.getBirthDate());
		if (pet.getType() != null) {
			PetTypeJpaEntity pt = new PetTypeJpaEntity();
			pt.setId(pet.getType().getId());
			pt.setName(pet.getType().getName());
			e.setType(pt);
		}
		pet.getVisits().forEach(v -> {
			VisitJpaEntity ve = new VisitJpaEntity();
			ve.setId(v.getId());
			ve.setDate(v.getDate());
			ve.setDescription(v.getDescription());
			e.getVisits().add(ve);
		});
		return e;
	}

}
