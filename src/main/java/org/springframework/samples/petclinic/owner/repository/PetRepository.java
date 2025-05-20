package org.springframework.samples.petclinic.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.owner.model.Pet;

public interface PetRepository extends JpaRepository<Pet, Integer> {

}
