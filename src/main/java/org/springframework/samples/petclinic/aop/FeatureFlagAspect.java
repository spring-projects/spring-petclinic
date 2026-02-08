package org.springframework.samples.petclinic.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.services.FeatureFlagService;
import org.springframework.samples.petclinic.system.FeatureFlag;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Aspect
@Component
public class FeatureFlagAspect {
	@Autowired
	private FeatureFlagService featureFlagService;

	@Around("@annotation(featureFlag)")
	public Object checkFeatureFlag(ProceedingJoinPoint joinPoint, FeatureFlag featureFlag) throws Throwable {
		String flagKey = featureFlag.value();
		String userEmail = getCurrentUserEmail();

		if (!featureFlagService.isFeatureEnabled(flagKey, userEmail)) {
			HttpServletResponse response = getResponse();
			response.setStatus(403);
			return "redirect:/oups"; // PetClinic error page
		}

		return joinPoint.proceed();
	}

	private String getCurrentUserEmail() {
		// Simple: use fixed email or extract from session/request
		// For demo: return "test@example.com"
		HttpServletRequest request = getRequest();
		String email = request.getParameter("email");
		return email != null ? email : "test@example.com";
	}

	private HttpServletRequest getRequest() {
		ServletRequestAttributes attributes =
			(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attributes.getRequest();
	}

	private HttpServletResponse getResponse() {
		ServletRequestAttributes attributes =
			(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attributes.getResponse();
	}
}
