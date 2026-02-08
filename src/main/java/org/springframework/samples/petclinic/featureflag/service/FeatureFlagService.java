package org.springframework.samples.petclinic.featureflag.service;

import org.springframework.samples.petclinic.featureflag.entity.FeatureFlag;
import org.springframework.samples.petclinic.featureflag.repository.FeatureFlagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeatureFlagService {

	private final FeatureFlagRepository repo;

	public FeatureFlagService(FeatureFlagRepository repo) {
		this.repo = repo;
	}

	public FeatureFlag createFeatureFlag(FeatureFlag flag) {
		validatePercentageRollout(flag);
		return repo.save(flag);
	}

	public List<FeatureFlag> fetchAllFeatureFlags() {
		return repo.findAll();
	}

	public FeatureFlag getFeatureFlagByName(String name) {
		return repo.findByFlagName(name).orElseThrow();
	}

	public Optional<FeatureFlag> updateFeatureFlag(Long id, FeatureFlag flag) {
		Optional<FeatureFlag> optionalFlag = repo.findById(id);
		if (optionalFlag.isEmpty()) {
			return Optional.empty();
		}

		FeatureFlag f = optionalFlag.get();

		if (flag.getFlagDescription() != null) {
			f.setFlagDescription(flag.getFlagDescription());
		}

		f.setFlagEnabled(flag.isFlagEnabled());
		f.setRolloutPercentage(flag.getRolloutPercentage());
		f.setWhitelistUsers(flag.getWhitelistUsers());
		f.setBlacklistUsers(flag.getBlacklistUsers());

		FeatureFlag saved = repo.save(f);

		return Optional.of(saved);
	}

	public void deleteFeatureFlag(Long id) {
		repo.deleteById(id);
	}

	private void validatePercentageRollout(FeatureFlag flag) {
		Integer pct = flag.getRolloutPercentage();
		if (pct != null && (pct < 0 || pct > 100)) {
			throw new IllegalArgumentException("rolloutPercentage must be 0-100");
		}
	}

}
