/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system;

import jakarta.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
public class FeatureFlagAspect {

	private final FeatureFlagHelper featureFlagHelper;

	private final HttpServletRequest request;

	public FeatureFlagAspect(FeatureFlagHelper featureFlagHelper, HttpServletRequest request) {
		this.featureFlagHelper = featureFlagHelper;
		this.request = request;
	}

	@Around("@annotation(featureFlagEnabled)")
	public Object checkFeatureFlag(ProceedingJoinPoint joinPoint, FeatureFlagEnabled featureFlagEnabled)
			throws Throwable {
		String featureFlagKey = featureFlagEnabled.value();
		Integer ownerId = extractOwnerIdFromPath();
		boolean enabled = featureFlagHelper.isEnabled(featureFlagKey, ownerId);
		if (!enabled) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Feature '" + featureFlagKey + "' is disabled");
		}
		return joinPoint.proceed();
	}

	private Integer extractOwnerIdFromPath() {
		String uri = request.getRequestURI();
		String[] parts = uri.split("/");
		for (int i = 0; i < parts.length - 1; i++) {
			if ("owners".equals(parts[i])) {
				try {
					return Integer.parseInt(parts[i + 1]);
				}
				catch (NumberFormatException e) {
					return null;
				}
			}
		}
		return null;
	}

}
