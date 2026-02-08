package org.springframework.samples.petclinic.featureflag.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class FeatureFlagDTO {

	@NotBlank
	private String flagKey;

	@NotBlank
	private String description;

	private boolean enabled;

	private String flagType; // SIMPLE, WHITELIST, BLACKLIST, PERCENTAGE

	@Min(0)
	@Max(100)
	private Integer percentage;

	private Set<String> whitelist = new HashSet<>();

	private Set<String> blacklist = new HashSet<>();

	// Getters & Setters

	public String getFlagKey() {
		return flagKey;
	}

	public void setFlagKey(String flagKey) {
		this.flagKey = flagKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getFlagType() {
		return flagType;
	}

	public void setFlagType(String flagType) {
		this.flagType = flagType;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public Set<String> getWhitelist() {
		return whitelist;
	}

	public void setWhitelist(Set<String> whitelist) {
		this.whitelist = whitelist;
	}

	public Set<String> getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(Set<String> blacklist) {
		this.blacklist = blacklist;
	}

}
