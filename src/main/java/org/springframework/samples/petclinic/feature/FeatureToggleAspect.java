package org.springframework.samples.petclinic.feature;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
public class FeatureToggleAspect {

	private final FeatureFlagService featureFlagService;

	public FeatureToggleAspect(FeatureFlagService featureFlagService) {
		this.featureFlagService = featureFlagService;
	}

	@Before("@annotation(featureToggle)")
	public void checkFeature(JoinPoint joinPoint, FeatureToggle featureToggle) {
		String user = "anonymous"; // can be replaced later
		if (!featureFlagService.isEnabled(featureToggle.value(), user)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"Feature " + featureToggle.value() + " is disabled");
		}
	}

}
