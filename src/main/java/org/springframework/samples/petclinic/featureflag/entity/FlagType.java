package org.springframework.samples.petclinic.featureflag.entity;

public enum FlagType {

	SIMPLE, // on/off flag
	WHITELIST, // only whitelisted users have access
	BLACKLIST, // all users have access except blacklisted ones
	PERCENTAGE, // Gradual rollout based on percentage of users
	GLOBAL_DISABLE // Override to disable globally regardless of other settings

}
