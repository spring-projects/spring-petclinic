package org.springframework.samples.petclinic.adapters.repositories.owner;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.adapters.entities.owner.OwnerJpaEntity;

interface OwnerSpringDataRepository extends JpaRepository<OwnerJpaEntity, Integer> {

	Optional<OwnerJpaEntity> findById(Integer id);

	Page<OwnerJpaEntity> findByLastNameStartingWith(String lastName, Pageable pageable);

}
