package org.springframework.samples.petclinic.owner;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link NewPet} entities.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations, pagination, and JPA-specific
 * functionality.
 * </p>
 *
 * @author You
 * @since 1.0
 */
public interface NewPetRepository extends JpaRepository<NewPet, Integer> {

	/**
	 * Finds the pet with the given id.
	 * @param id The id of the pet to search for.
	 * @return a pet with the specified id.
	 *
	 */
	NewPet findById(int id);

	/**
	 * Saves the given pet
	 * @param newPet The pet object to be saved.
	 * @return pet object being saved.
	 *
	 */
	NewPet save(NewPet newPet);

}
