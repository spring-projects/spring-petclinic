package org.springframework.samples.petclinic.owner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetFeaturesRepository extends JpaRepository<PetFeatures, Integer> {



}
