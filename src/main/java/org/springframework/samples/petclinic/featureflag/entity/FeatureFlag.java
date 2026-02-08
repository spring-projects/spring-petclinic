package org.springframework.samples.petclinic.featureflag.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "feature_flags", uniqueConstraints = { @UniqueConstraint(columnNames = "flag_key") })
public class FeatureFlag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "flag_key", nullable = false, unique = true, updatable = false)
	private String flagKey;

	@NotBlank
	@Column(name = "description", nullable = false)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "flag_type", nullable = false)
	private FlagType flagType = FlagType.SIMPLE;

	@Column(name = "enabled", nullable = false)
	private boolean enabled = false;

	/*
	 * Used only for percentage rollouts, represents the percentage of users that should
	 * have access to the feature. Should be a value between 0 and 100. Ignored for other
	 * flag types.
	 *
	 */
	@Min(0)
	@Max(100)
	@Column(name = "percentage")
	private Integer percentage;

	/*
	 * Explicit allow-list
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "feature_flag_whitelist", joinColumns = @JoinColumn(name = "feature_flag_id"))
	private Set<String> whitelist = new HashSet<>();

	/*
	 * Explicit deny-list (highest priority, overrides both percentage and allow-list)
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "feature_flag_blacklist", joinColumns = @JoinColumn(name = "feature_flag_id"))
	private Set<String> blacklist = new HashSet<>();

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = this.createdAt;
		this.flagKey = this.flagKey.toUpperCase();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

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

	public FlagType getFlagType() {
		return flagType;
	}

	public void setFlagType(FlagType flagType) {
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

}
