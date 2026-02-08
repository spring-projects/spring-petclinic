package org.springframework.samples.petclinic.feature;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Integer> {

	Optional<FeatureFlag> findByName(String name);

}
