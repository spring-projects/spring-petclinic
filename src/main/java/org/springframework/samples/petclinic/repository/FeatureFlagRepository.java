package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.model.FeatureFlag;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
	Optional<FeatureFlag> findByFlagKey(String flagKey);
}
