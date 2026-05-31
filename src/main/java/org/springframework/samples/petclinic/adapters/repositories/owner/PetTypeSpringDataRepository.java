package org.springframework.samples.petclinic.adapters.repositories.owner;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.adapters.entities.owner.PetTypeJpaEntity;

interface PetTypeSpringDataRepository extends JpaRepository<PetTypeJpaEntity, Integer> {

	@Query("SELECT pt FROM PetTypeJpaEntity pt ORDER BY pt.name")
	List<PetTypeJpaEntity> findAllOrderedByName();

	Optional<PetTypeJpaEntity> findByName(String name);

}
