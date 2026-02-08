package org.springframework.samples.petclinic.featureflag.dto;

public class FeatureCheckResponse {
    private String flagKey;

    private Boolean enabled;

    private String context;

    public FeatureCheckResponse(String flagKey, Boolean enabled, String context) {
        this.flagKey = flagKey;
        this.enabled = enabled;
        this.context = context;
    }

    public String getFlagKey() {
        return flagKey;
    }

    public void setFlagKey(String flagKey) {
        this.flagKey = flagKey;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
