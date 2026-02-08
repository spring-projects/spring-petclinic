package org.springframework.samples.petclinic.featureflag.dto;

public class FeatureCheckRequest {

    private String flagKey;
    private String context;

    public String getFlagKey() {
        return flagKey;
    }

    public void setFlagKey(String flagKey) {
        this.flagKey = flagKey;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

}
