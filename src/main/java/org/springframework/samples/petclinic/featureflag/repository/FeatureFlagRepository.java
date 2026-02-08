package org.springframework.samples.petclinic.featureflag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.featureflag.entity.FeatureFlag;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {

	Optional<FeatureFlag> findByFlagKey(String flagKey);

	boolean existsByFlagKey(String flagKey);

}
