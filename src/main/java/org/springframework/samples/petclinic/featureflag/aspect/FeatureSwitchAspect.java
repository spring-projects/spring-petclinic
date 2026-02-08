package org.springframework.samples.petclinic.featureflag.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.featureflag.annotation.FeatureSwitch;
import org.springframework.samples.petclinic.featureflag.constants.FeatureFlagConstants;
import org.springframework.samples.petclinic.featureflag.service.FeatureEvaluationService;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FeatureSwitchAspect {

	@Autowired
	public FeatureEvaluationService featureEvaluationService;

	@Around("@annotation(featureSwitch)")
	public Object checkFeatureFlag(ProceedingJoinPoint joinPoint, FeatureSwitch featureSwitch) throws Throwable {
		String user = FeatureFlagConstants.ANONYMOUS_USER;
		if (featureEvaluationService.isFlagEnabledForUser(featureSwitch.value(), user)) {
			return joinPoint.proceed();
		}
		else {
			throw new RuntimeException("Feature " + featureSwitch.value() + " is disabled");
		}
	}

}
