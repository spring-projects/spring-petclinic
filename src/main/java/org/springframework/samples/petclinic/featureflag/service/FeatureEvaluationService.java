package org.springframework.samples.petclinic.featureflag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.featureflag.entity.FeatureFlag;
import org.springframework.samples.petclinic.featureflag.repository.FeatureFlagRepository;
import org.springframework.stereotype.Service;

/**
 * Service responsible for evaluating feature flags. Supports global enable/disable,
 * whitelist, blacklist, and percentage rollout strategies.
 */
@Service
public class FeatureEvaluationService {

	@Autowired
	private FeatureFlagRepository featureFlagRepository;

	/**
	 * Determines whether a feature is enabled for a given user.
	 *
	 * Evaluation order: 1. Global disable check 2. Blacklist override 3. Whitelist
	 * override 4. Percentage rollout
	 */
	public boolean isFlagEnabledForUser(String flagName, String userId) {
		FeatureFlag flag = featureFlagRepository.findByFlagName(flagName).orElse(null);

		if (flag == null)
			return false;
		if (!flag.isFlagEnabled())
			return false;

		if (flag.getBlacklistUsers().contains(userId))
			return false;

		if (flag.getWhitelistUsers().contains(userId))
			return true;

		Integer rolloutPercentage = flag.getRolloutPercentage();
		if (rolloutPercentage != null) {
			if (rolloutPercentage < 0 || rolloutPercentage > 100)
				return false;
			int bucket = Math.abs(userId.hashCode() % 100);
			return bucket < rolloutPercentage;
		}

		return true;
	}

}
