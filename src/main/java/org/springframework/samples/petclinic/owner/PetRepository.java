package org.springframework.samples.petclinic.owner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Thibault Helsmoortel
 */
public interface PetRepository extends Repository<Pet, Integer> {

	@Query("SELECT DISTINCT pet FROM Pet pet WHERE lower(pet.name) LIKE :name% ")
	@Transactional(readOnly = true)
	Page<Pet> findByName(String name, Pageable pageable);

	Pet findById(Integer id);

}
