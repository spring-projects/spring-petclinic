package org.springframework.samples.petclinic.owner;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository class for <code>PetType</code> domain object.
 * @author er-satish
 */
public interface PetTypeRepository extends JpaRepository<PetType,Integer> {

}
