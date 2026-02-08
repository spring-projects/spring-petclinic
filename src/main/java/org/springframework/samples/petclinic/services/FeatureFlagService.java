package org.springframework.samples.petclinic.services;

//package com.example.petclinic.system;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dto.FlagDTO;
import org.springframework.samples.petclinic.model.FeatureFlag;
import org.springframework.samples.petclinic.repository.FeatureFlagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeatureFlagService {
	@Autowired
	private FeatureFlagRepository repository;

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final SecureRandom random = new SecureRandom();

	public List<FeatureFlag> getAllFlags() {
		return repository.findAll();
	}

	public Optional<FeatureFlag> getFlag(String flagKey) {
		return repository.findByFlagKey(flagKey);
	}

	public FeatureFlag saveFlag(FlagDTO dto) {
		FeatureFlag flag = repository.findByFlagKey(dto.flagKey).orElse(new FeatureFlag());

		flag.setFlagKey(dto.flagKey);
		flag.setName(dto.name);
		flag.setEnabled(dto.enabled != null ? dto.enabled : true);
		flag.setStrategyType(dto.strategyType != null ? dto.strategyType : FeatureFlag.StrategyType.GLOBAL);
		flag.setStrategyValue(dto.strategyValue);

		flag.setUpdatedAt(LocalDateTime.now());
		if (flag.getCreatedAt() == null) {
			flag.setCreatedAt(LocalDateTime.now());
		}

		return repository.save(flag);
	}


	public void deleteFlag(String flagKey) {
		repository.findByFlagKey(flagKey).ifPresent(repository::delete);
	}

	// 🔥 THE HELPER FUNCTION - Call this anywhere!
	public boolean isFeatureEnabled(String flagKey, String userEmail) {
		return getFlag(flagKey)
			.filter(FeatureFlag::isEnabled)
			.map(flag -> evaluateStrategy(flag, userEmail))
			.orElse(true); // Default ON if flag doesn't exist
	}

	private boolean evaluateStrategy(FeatureFlag flag, String userEmail) {
		try {
			switch (flag.getStrategyType()) {
				case GLOBAL:
					return true;
				case BLACKLIST:
					return !isUserInList(flag.getStrategyValue(), userEmail);
				case WHITELIST:
					return isUserInList(flag.getStrategyValue(), userEmail);
				case PERCENTAGE:
					return isUserInPercentage(flag.getStrategyValue());
				default:
					return true;
			}
		} catch (Exception e) {
			return true; // Fail open
		}
	}

	private boolean isUserInList(String strategyValue, String userEmail) {
		if (strategyValue == null || strategyValue.isEmpty()) return true;
		JsonNode users = parseJson(strategyValue).get("users");
		return users != null && users.has(userEmail);
	}

	private boolean isUserInPercentage(String strategyValue) {
		if (strategyValue == null || strategyValue.isEmpty()) return true;
		JsonNode node = parseJson(strategyValue);
		int percentage = node.get("percentage").asInt(100);
		return random.nextInt(100) < percentage;
	}

	private JsonNode parseJson(String json) {
		try {
			return objectMapper.readTree(json);
		} catch (Exception e) {
			return objectMapper.createObjectNode();
		}
	}
}
