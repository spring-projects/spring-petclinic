package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.model.PetTypes;

public interface PetTypeRepository extends JpaRepository<PetTypes, Long> {
}
