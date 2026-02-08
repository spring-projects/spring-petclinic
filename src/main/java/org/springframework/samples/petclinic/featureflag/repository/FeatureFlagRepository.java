package org.springframework.samples.petclinic.featureflag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.featureflag.entity.FeatureFlag;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {

	Optional<FeatureFlag> findByFlagName(String flagName);

}
