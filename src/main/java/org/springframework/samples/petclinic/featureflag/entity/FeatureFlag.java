package org.springframework.samples.petclinic.featureflag.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "feature_flags")
public class FeatureFlag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long flagId;

	private String flagName;

	private boolean flagEnabled;

	private Integer rolloutPercentage;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "flag_whitelist")
	private Set<String> whitelistUsers = new HashSet<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "flag_blacklist")
	private Set<String> blacklistUsers = new HashSet<>();

	private String flagDescription;

	public FeatureFlag() {
	}

	public Long getFlagId() {
		return flagId;
	}

	public void setFlagId(Long flagId) {
		this.flagId = flagId;
	}

	public String getFlagName() {
		return flagName;
	}

	public void setFlagName(String flagName) {
		this.flagName = flagName;
	}

	public boolean isFlagEnabled() {
		return flagEnabled;
	}

	public void setFlagEnabled(boolean flagEnabled) {
		this.flagEnabled = flagEnabled;
	}

	public Integer getRolloutPercentage() {
		return rolloutPercentage;
	}

	public void setRolloutPercentage(Integer rolloutPercentage) {
		this.rolloutPercentage = rolloutPercentage;
	}

	public Set<String> getWhitelistUsers() {
		return whitelistUsers;
	}

	public void setWhitelistUsers(Set<String> whitelistUsers) {
		this.whitelistUsers = whitelistUsers;
	}

	public Set<String> getBlacklistUsers() {
		return blacklistUsers;
	}

	public void setBlacklistUsers(Set<String> blacklistUsers) {
		this.blacklistUsers = blacklistUsers;
	}

	public String getFlagDescription() {
		return flagDescription;
	}

	public void setFlagDescription(String flagDescription) {
		this.flagDescription = flagDescription;
	}

	@Override
	public String toString() {
		return "FeatureFlag{" + "flagId=" + flagId + ", flagName='" + flagName + '\'' + ", isFlagEnabled=" + flagEnabled
				+ ", rolloutPercentage=" + rolloutPercentage + ", whitelistUsers=" + whitelistUsers
				+ ", blacklistUsers=" + blacklistUsers + ", flagDescription='" + flagDescription + '\'' + '}';
	}

}
