package org.springframework.samples.petclinic.owner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.owner.model.PetAttributes;

/**
 * Repository class for <code>PetAttributes</code> domain objects. Provides CRUD
 * operations and custom query methods related to a pet's attributes.
 *
 * @see org.springframework.samples.petclinic.owner.model.PetAttributes
 */
public interface PetAttributesRepository extends JpaRepository<PetAttributes, Integer> {

	/**
	 * Find pet attributes by pet ID.
	 *
	 * @param petId the ID of the pet
	 * @return an Optional containing the PetAttributes if found
	 */
	Optional<PetAttributes> findByPetId(Integer petId);

}
