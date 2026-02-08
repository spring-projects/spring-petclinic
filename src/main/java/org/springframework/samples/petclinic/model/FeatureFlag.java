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

package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "feature_flags")
public class FeatureFlag extends BaseEntity {

	@Column(name = "flag_key", unique = true)
	@NotBlank
	private String key;

	@Column(name = "is_enabled")
	private boolean isEnabled;

	@Column(name = "globally_disabled")
	private boolean globallyDisabled;

	@Column(name = "rollout_percentage")
	private Integer rolloutPercentage;

	@Column(name = "description")
	private String description;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "feature_flag_blacklist", joinColumns = @JoinColumn(name = "feature_flag_id"))
	@JsonIgnore
	@Column(name = "owner_id")
	private List<Integer> blacklistedUsers = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "feature_flag_whitelist", joinColumns = @JoinColumn(name = "feature_flag_id"))
	@JsonIgnore
	@Column(name = "owner_id")
	private List<Integer> whitelistedUsers = new ArrayList<>();

	public boolean isAllowedForOwner(Integer ownerId) {
		if (globallyDisabled) {
			return false;
		}

		if (ownerId != null && blacklistedUsers.contains(ownerId)) {
			return false;
		}

		if (ownerId != null && whitelistedUsers.contains(ownerId)) {
			return true;
		}

		if (rolloutPercentage != null && ownerId != null) {
			int bucket = Math.abs(ownerId.hashCode() % 100);
			return bucket < rolloutPercentage;
		}
		return isEnabled;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
	}

	public boolean isGloballyDisabled() {
		return globallyDisabled;
	}

	public void setGloballyDisabled(boolean globallyDisabled) {
		this.globallyDisabled = globallyDisabled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	public List<Integer> getBlacklistedUsers() {
		return blacklistedUsers;
	}

	public void setBlacklistedUsers(List<Integer> blacklistedUsers) {
		this.blacklistedUsers = blacklistedUsers;
	}

	@JsonIgnore
	public List<Integer> getWhitelistedUsers() {
		return whitelistedUsers;
	}

	public void setWhitelistedUsers(List<Integer> whitelistedUsers) {
		this.whitelistedUsers = whitelistedUsers;
	}

}
