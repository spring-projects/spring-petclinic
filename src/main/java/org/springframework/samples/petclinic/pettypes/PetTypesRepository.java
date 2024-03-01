package org.springframework.samples.petclinic.pettypes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.owner.PetType;

public interface PetTypesRepository extends JpaRepository<PetType, Integer> {

}
