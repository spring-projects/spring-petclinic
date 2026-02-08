package org.springframework.samples.petclinic.featureflag.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.featureflag.constants.FeatureFlagConstants;
import org.springframework.samples.petclinic.featureflag.service.FeatureEvaluationService;
import org.springframework.stereotype.Component;

@Component
public class FeatureFlagHelper {

	@Autowired
	private FeatureEvaluationService service;

	public boolean isEnabledForUser(String flagName) {
		String user = getCurrentUser();
		return service.isFlagEnabledForUser(flagName, user);
	}

	private String getCurrentUser() {
		return FeatureFlagConstants.ANONYMOUS_USER;
	}

}
