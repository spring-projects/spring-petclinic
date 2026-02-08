package org.springframework.samples.petclinic.featureflag.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.featureflag.annotation.FeatureToggle;
import org.springframework.samples.petclinic.featureflag.service.FeatureFlagService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * AOP Aspect that intercepts methods annotated with @FeatureToggle and checks if the
 * feature is enabled before allowing execution
 */
@Aspect
@Component
public class FeatureToggleAspect {

	private static final Logger logger = LoggerFactory.getLogger(FeatureToggleAspect.class);

	private final FeatureFlagService featureFlagService;

	public FeatureToggleAspect(FeatureFlagService featureFlagService) {
		this.featureFlagService = featureFlagService;
	}

	@Around("@annotation(org.springframework.samples.petclinic.featureflag.annotation.FeatureToggle)")
	public Object checkFeatureToggle(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		FeatureToggle featureToggle = method.getAnnotation(FeatureToggle.class);

		String flagKey = featureToggle.key();
		String contextParam = featureToggle.contextParam();

		// Extract context if specified
		String context = null;
		if (!contextParam.isEmpty()) {
			context = extractContextFromParams(method, joinPoint.getArgs(), contextParam);
		}

		// Check if feature is enabled
		logger.debug("Checking feature toggle '{}' with context '{}'", flagKey, context);
		boolean isEnabled = featureFlagService.isFeatureEnabled(flagKey, context);

		logger.debug("Feature toggle '{}' check: enabled={}, context={}", flagKey, isEnabled, context);

		if (isEnabled) {
			// Feature is enabled, proceed with method execution
			return joinPoint.proceed();
		}
		else {
			// Feature is disabled, handle based on configuration
			logger.info("Feature '{}' is disabled, blocking execution", flagKey);
			return handleDisabledFeature(joinPoint, featureToggle);
		}
	}

	/**
	 * Extract context value from method parameters
	 */
	private String extractContextFromParams(Method method, Object[] args, String paramName) {
		Parameter[] parameters = method.getParameters();

		for (int i = 0; i < parameters.length; i++) {
			// Check if parameter name matches (requires -parameters compiler flag)
			if (parameters[i].getName().equals(paramName)) {
				if (args[i] != null) {
					return args[i].toString();
				}
			}

			// Also check for @RequestParam, @PathVariable annotations
			for (var annotation : parameters[i].getAnnotations()) {
				String annotationStr = annotation.toString();
				if (annotationStr.contains(paramName)) {
					if (args[i] != null) {
						return args[i].toString();
					}
				}
			}
		}

		return null;
	}

	/**
	 * Handle disabled feature - redirect or return error view
	 */
	private Object handleDisabledFeature(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) {
		String redirect = featureToggle.disabledRedirect();
		String message = featureToggle.disabledMessage();

		// Look for RedirectAttributes in method parameters
		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			if (arg instanceof RedirectAttributes redirectAttrs) {
				redirectAttrs.addFlashAttribute("message", message);
				break;
			}
		}

		// If redirect is specified, return redirect
		if (!redirect.isEmpty()) {
			return "redirect:" + redirect;
		}

		// Otherwise return to a generic disabled feature page or home
		return "redirect:/oups"; // PetClinic error page
	}

}