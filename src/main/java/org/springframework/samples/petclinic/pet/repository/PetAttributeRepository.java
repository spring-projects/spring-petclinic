package org.springframework.samples.petclinic.pet.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.pet.model.PetAttribute;

import java.util.List;

/**
 * @author Rohit Lalwani
 */
public interface PetAttributeRepository extends CrudRepository<PetAttribute, Integer> {

	@EntityGraph(attributePaths = "petType")
	List<PetAttribute> findByPetTypeId(Integer typeId);

}
