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
        private Set<String> listItems;

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

        public Set<String> getListItems() {
            return listItems;
        }

        public void setListItems(Set<String> listItems) {
            this.listItems = listItems;
        }

        public FeatureFlag toEntity() {
            FeatureFlag flag = new FeatureFlag();
            flag.setFlagKey(this.flagKey);
            flag.setDescription(this.description);
            flag.setFlagType(this.flagType != null ? this.flagType : FlagType.SIMPLE);
            flag.setEnabled(this.enabled != null ? this.enabled : false);
            flag.setPercentage(this.percentage);
            flag.setBlacklist(this.listItems != null ? this.listItems : Set.of());
            flag.setWhitelist(this.listItems != null ? this.listItems : Set.of());
            return flag;
        }
}
