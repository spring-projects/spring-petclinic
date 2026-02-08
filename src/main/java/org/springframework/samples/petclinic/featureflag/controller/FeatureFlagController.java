package org.springframework.samples.petclinic.featureflag.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.featureflag.entity.FeatureFlag;
import org.springframework.samples.petclinic.featureflag.service.FeatureFlagService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.samples.petclinic.featureflag.dto.*;

@RestController
@RequestMapping("/feature-flags")
public class FeatureFlagController {

	private final FeatureFlagService featureFlagService;

	public FeatureFlagController(FeatureFlagService featureFlagService) {
		this.featureFlagService = featureFlagService;
	}

	/**
	 * GET /api/feature-flags Get all feature flags
	 */
	@GetMapping
	public ResponseEntity<List<FeatureFlagResponse>> getAllFlags() {
		List<FeatureFlagResponse> flags = featureFlagService.getAllFlags()
			.stream()
			.map(FeatureFlagResponse::fromEntity)
			.collect(Collectors.toList());
		return ResponseEntity.ok(flags);
	}

	/**
	 * GET /api/feature-flags/{id} Get a specific feature flag by ID
	 */
	@GetMapping("/{id}")
	public ResponseEntity<FeatureFlagResponse> getFlagById(@PathVariable Long id) {
		return featureFlagService.getFlagById(id)
			.map(FeatureFlagResponse::fromEntity)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * GET /api/feature-flags/key/{flagKey} Get a specific feature flag by key
	 */
	@GetMapping("/key/{flagKey}")
	public ResponseEntity<FeatureFlagResponse> getFlagByKey(@PathVariable String flagKey) {
		return featureFlagService.getFlagByKey(flagKey)
			.map(FeatureFlagResponse::fromEntity)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * POST /api/feature-flags Create a new feature flag
	 */
	@PostMapping
	public ResponseEntity<?> createFlag(@RequestBody FeatureFlagRequest request) {
		try {
			FeatureFlag flag = request.toEntity();
			FeatureFlag created = featureFlagService.createFlag(flag);
			return ResponseEntity.status(HttpStatus.CREATED).body(FeatureFlagResponse.fromEntity(created));
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	/**
	 * PUT /api/feature-flags/{id} Update an existing feature flag
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateFlag(@PathVariable Long id, @RequestBody FeatureFlagRequest request) {
		try {
			FeatureFlag flag = request.toEntity();
			FeatureFlag updated = featureFlagService.updateFlag(id, flag);
			return ResponseEntity.ok(FeatureFlagResponse.fromEntity(updated));
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	/**
	 * DELETE /api/feature-flags/{id} Delete a feature flag
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteFlag(@PathVariable Long id) {
		try {
			featureFlagService.deleteFlag(id);
			return ResponseEntity.noContent().build();
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	/**
	 * POST /api/feature-flags/{flagKey}/toggle Toggle a feature flag on/off
	 */
	@PostMapping("/{flagKey}/toggle")
	public ResponseEntity<?> toggleFlag(@PathVariable String flagKey) {
		try {
			FeatureFlag toggled = featureFlagService.toggleFlag(flagKey);
			return ResponseEntity.ok(FeatureFlagResponse.fromEntity(toggled));
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	/**
	 * POST /api/feature-flags/check Check if a feature is enabled for a given context
	 */
	@PostMapping("/check")
	public ResponseEntity<FeatureCheckResponse> checkFeature(@RequestBody FeatureCheckRequest request) {
		boolean enabled = featureFlagService.isFeatureEnabled(request.getFlagKey(), request.getContext());
		FeatureCheckResponse response = new FeatureCheckResponse(request.getFlagKey(), enabled, request.getContext());
		return ResponseEntity.ok(response);
	}

	/**
	 * GET /api/feature-flags/check/{flagKey} Check if a feature is enabled (simple check
	 * without context)
	 */
	@GetMapping("/check/{flagKey}")
	public ResponseEntity<FeatureCheckResponse> checkFeatureSimple(@PathVariable String flagKey) {
		boolean enabled = featureFlagService.isFeatureEnabled(flagKey);
		FeatureCheckResponse response = new FeatureCheckResponse(flagKey, enabled, null);
		return ResponseEntity.ok(response);
	}

	/**
	 * Error response class
	 */
	public static class ErrorResponse {

		private String error;

		public ErrorResponse(String error) {
			this.error = error;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

	}

}
