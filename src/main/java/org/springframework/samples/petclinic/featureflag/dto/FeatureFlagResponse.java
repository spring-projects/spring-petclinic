package org.springframework.samples.petclinic.featureflag.dto;

import java.util.Set;

import org.springframework.samples.petclinic.featureflag.entity.FeatureFlag;
import org.springframework.samples.petclinic.featureflag.entity.FlagType;

public class FeatureFlagResponse {

    private Long id;
    private String flagKey;
    private String description;
    private FlagType flagType;
    private Boolean enabled;
    private Integer percentage;
    private Set<String> whitelist;
    private Set<String> blacklist;
    private String createdAt;
    private String updatedAt;

    public static FeatureFlagResponse fromEntity(FeatureFlag flag) {
        FeatureFlagResponse response = new FeatureFlagResponse();
        response.setId(flag.getId());
        response.setFlagKey(flag.getFlagKey());
        response.setDescription(flag.getDescription());
        response.setFlagType(flag.getFlagType());
        response.setEnabled(flag.isEnabled());
        response.setPercentage(flag.getPercentage());
        response.setWhiteList(flag.getWhitelist());
        response.setBlackList(flag.getBlacklist());
        response.setCreatedAt(flag.getCreatedAt().toString());
        response.setUpdatedAt(flag.getUpdatedAt().toString());
        return response;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public FlagType getFlagType() {
        return flagType;
    }

    public void setFlagType(FlagType flagType) {
        this.flagType = flagType;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public Set<String> getWhiteList() {
        return whitelist;
    }

    public void setWhiteList(Set<String> listItems) {
        this.whitelist = listItems;
    }

    public Set<String> getBlackList() {
        return blacklist;
    }

    public void setBlackList(Set<String> listItems) {
        this.blacklist = listItems;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
