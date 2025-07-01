package org.springframework.samples.petclinic.owner;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetAttributesRepository extends JpaRepository<PetAttributes, Integer> {

	PetAttributes findByPetId(int petId);

}
