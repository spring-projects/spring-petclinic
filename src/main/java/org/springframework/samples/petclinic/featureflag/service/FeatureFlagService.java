package org.springframework.samples.petclinic.featureflag.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.featureflag.entity.FeatureFlag;
import org.springframework.samples.petclinic.featureflag.entity.FlagType;
import org.springframework.samples.petclinic.featureflag.repository.FeatureFlagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Feature Flag Service - Core service for managing and evaluating feature flags
 *
 * This service provides: - CRUD operations for feature flags - Advanced flag evaluation
 * with multiple strategies - Helper methods for easy integration anywhere in the
 * application
 */
@Service
@Transactional
public class FeatureFlagService {

	private static final Logger logger = LoggerFactory.getLogger(FeatureFlagService.class);

	private final FeatureFlagRepository repository;

	public FeatureFlagService(FeatureFlagRepository repository) {
		this.repository = repository;
	}

	/**
	 * Main helper function to check if a feature is enabled Can be called from anywhere
	 * in the application
	 * @param flagKey The unique identifier for the feature flag
	 * @return true if feature is enabled, false otherwise
	 */
	public boolean isFeatureEnabled(String flagKey) {
		return isFeatureEnabled(flagKey, null);
	}

	/**
	 * Check if feature is enabled for a specific context/user
	 * @param flagKey The unique identifier for the feature flag
	 * @param context Context identifier (e.g., userId, sessionId, email)
	 * @return true if feature is enabled for this context, false otherwise
	 */
	public boolean isFeatureEnabled(String flagKey, String context) {
		try {
			Optional<FeatureFlag> flagOpt = repository.findByFlagKey(flagKey.toUpperCase());

			if (flagOpt.isEmpty()) {
				logger.warn("Feature flag '{}' not found, defaulting to disabled", flagKey);
				return false;
			}

			FeatureFlag flag = flagOpt.get();
			return evaluateFlag(flag, context);

		}
		catch (Exception e) {
			logger.error("Error evaluating feature flag '{}': {}", flagKey, e.getMessage());
			// Fail safe: return false on errors
			return false;
		}
	}

	/**
	 * Evaluate a feature flag based on its type and configuration Evaluation order
	 * (highest to lowest priority): 1. GLOBAL_DISABLE - always returns false 2. Blacklist
	 * - if context is blacklisted, return false 3. Enabled check - if not enabled, return
	 * false 4. Type-specific evaluation (WHITELIST, PERCENTAGE, SIMPLE)
	 */
	private boolean evaluateFlag(FeatureFlag flag, String context) {
		// GLOBAL_DISABLE always returns false
		if (flag.getFlagType() == FlagType.GLOBAL_DISABLE) {
			logger.debug("Flag '{}' is globally disabled", flag.getFlagKey());
			return false;
		}

		// Check blacklist first (highest priority after GLOBAL_DISABLE)
		if (context != null && !context.trim().isEmpty()) {
			if (flag.getBlacklist().contains(context.trim())) {
				logger.debug("Flag '{}' - context '{}' is blacklisted", flag.getFlagKey(), context);
				return false;
			}
		}

		// If not enabled, return false (except for specific override cases)
		if (!flag.isEnabled()) {
			logger.debug("Flag '{}' is disabled", flag.getFlagKey());
			return false;
		}

		switch (flag.getFlagType()) {
			case SIMPLE:
				return true; // Simple on/off

			case WHITELIST:
				return evaluateWhitelist(flag, context);

			case BLACKLIST:
				// If we got here, context is not in blacklist and flag is enabled
				return true;

			case PERCENTAGE:
				return evaluatePercentage(flag, context);

			case GLOBAL_DISABLE:
				return false;

			default:
				logger.warn("Unknown flag type for '{}': {}", flag.getFlagKey(), flag.getFlagType());
				return false;
		}
	}

	/**
	 * Whitelist: Only allow if context is in the whitelist
	 */
	private boolean evaluateWhitelist(FeatureFlag flag, String context) {
		if (context == null || context.trim().isEmpty()) {
			logger.debug("Whitelist flag '{}' requires context, got null/empty", flag.getFlagKey());
			return false;
		}
		logger.debug("Whitelist flag '{}' - checking if context '{}' is in whitelist: {}", flag.getFlagKey(), context,
				flag.getWhitelist());
		boolean inWhitelist = flag.getWhitelist().contains(context.trim());
		logger.debug("Whitelist flag '{}' for context '{}': {}", flag.getFlagKey(), context, inWhitelist);
		return inWhitelist;
	}

	/**
	 * Percentage: Enable for X% of requests using consistent hashing
	 */
	private boolean evaluatePercentage(FeatureFlag flag, String context) {
		if (flag.getPercentage() == null || flag.getPercentage() < 0 || flag.getPercentage() > 100) {
			logger.warn("Invalid percentage for flag '{}': {}", flag.getFlagKey(), flag.getPercentage());
			return false;
		}

		if (flag.getPercentage() == 0) {
			return false;
		}

		if (flag.getPercentage() == 100) {
			return true;
		}

		// Use consistent hashing to ensure same context always gets same result
		String hashInput = flag.getFlagKey() + (context != null ? context : "");
		int hash = Math.abs(hashInput.hashCode());
		int bucket = hash % 100;

		boolean enabled = bucket < flag.getPercentage();
		logger.debug("Percentage flag '{}' for context '{}': bucket={}, percentage={}, enabled={}", flag.getFlagKey(),
				context, bucket, flag.getPercentage(), enabled);
		return enabled;
	}

	// CRUD Operations

	public List<FeatureFlag> getAllFlags() {
		return repository.findAll();
	}

	public Optional<FeatureFlag> getFlagById(Long id) {
		return repository.findById(id);
	}

	public Optional<FeatureFlag> getFlagByKey(String flagKey) {
		return repository.findByFlagKey(flagKey.toUpperCase());
	}

	public FeatureFlag createFlag(FeatureFlag flag) {
		if (repository.existsByFlagKey(flag.getFlagKey().toUpperCase())) {
			throw new IllegalArgumentException("Feature flag with key '" + flag.getFlagKey() + "' already exists");
		}

		validateFlag(flag);
		return repository.save(flag);
	}

	public FeatureFlag updateFlag(Long id, FeatureFlag updatedFlag) {
		FeatureFlag existingFlag = repository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Feature flag not found with id: " + id));

		// Don't allow changing the key
		updatedFlag.setFlagKey(existingFlag.getFlagKey());

		validateFlag(updatedFlag);
		return repository.save(updatedFlag);
	}

	public void deleteFlag(Long id) {
		if (!repository.existsById(id)) {
			throw new IllegalArgumentException("Feature flag not found with id: " + id);
		}
		repository.deleteById(id);
	}

	public FeatureFlag toggleFlag(String flagKey) {
		FeatureFlag flag = repository.findByFlagKey(flagKey.toUpperCase())
			.orElseThrow(() -> new IllegalArgumentException("Feature flag not found: " + flagKey));

		flag.setEnabled(!flag.isEnabled());
		return repository.save(flag);
	}

	/**
	 * Validate flag configuration
	 */
	private void validateFlag(FeatureFlag flag) {
		if (flag.getFlagKey() == null || flag.getFlagKey().trim().isEmpty()) {
			throw new IllegalArgumentException("Flag key cannot be empty");
		}

		if (flag.getFlagType() == FlagType.PERCENTAGE) {
			if (flag.getPercentage() == null || flag.getPercentage() < 0 || flag.getPercentage() > 100) {
				throw new IllegalArgumentException("Percentage must be between 0 and 100");
			}
		}

		if (flag.getFlagType() == FlagType.WHITELIST) {
			if (flag.getWhitelist() == null || flag.getWhitelist().isEmpty()) {
				logger.warn("Flag '{}' is of type WHITELIST but has no whitelist items", flag.getFlagKey());
			}
		}

		if (flag.getFlagType() == FlagType.BLACKLIST) {
			if (flag.getBlacklist() == null || flag.getBlacklist().isEmpty()) {
				logger.warn("Flag '{}' is of type BLACKLIST but has no blacklist items", flag.getFlagKey());
			}
		}
	}

}