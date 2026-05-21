package org.springframework.samples.petclinic.adapters.repositories.vet;

import java.util.Collection;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.adapters.entities.vet.VetEntity;
import org.springframework.transaction.annotation.Transactional;

interface VetSpringDataRepository extends Repository<VetEntity, Integer> {

	@Transactional(readOnly = true)
	@Cacheable("vets")
	Collection<VetEntity> findAll();

	@Transactional(readOnly = true)
	@Cacheable("vets")
	Page<VetEntity> findAll(Pageable pageable);

}
