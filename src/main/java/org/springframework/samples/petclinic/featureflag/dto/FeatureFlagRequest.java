package org.springframework.samples.petclinic.featureflag.dto;

import java.util.Set;

import org.springframework.samples.petclinic.featureflag.entity.FeatureFlag;
import org.springframework.samples.petclinic.featureflag.entity.FlagType;

public class FeatureFlagRequest {

    private String flagKey;

    private String description;

    private FlagType flagType;

    private Boolean enabled;

    private Integer percentage;

    private Set<String> whitelist;

    private Set<String> blacklist;

    // Getters and Setters
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

    /**
     * Convert DTO to Entity
     */
    public FeatureFlag toEntity() {
        FeatureFlag flag = new FeatureFlag();
        flag.setFlagKey(this.flagKey);
        flag.setDescription(this.description);
        flag.setFlagType(this.flagType != null ? this.flagType : FlagType.SIMPLE);
        flag.setEnabled(this.enabled != null ? this.enabled : false);
        flag.setPercentage(this.percentage);
        flag.setWhitelist(this.whitelist != null ? this.whitelist : Set.of());
        flag.setBlacklist(this.blacklist != null ? this.blacklist : Set.of());
        return flag;
    }
}
