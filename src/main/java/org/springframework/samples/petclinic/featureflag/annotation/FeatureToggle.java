package org.springframework.samples.petclinic.featureflag.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to mark methods protected by feature flags
 * 
 * Usage:
 * @FeatureToggle(key = "add-new-pet")
 * public String processNewPetForm(...) {
 *     // method implementation
 * }
 * 
 * With context extraction:
 * @FeatureToggle(key = "owner-search", contextParam = "name")
 * public String processFindForm(@RequestParam("name") String name, ...) {
 *     // method implementation
 * }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureToggle {
    
    /**
     * The feature flag key to check
     */
    String key();
    
    /**
     * Optional: name of the parameter to use as context
     * If specified, the value of this parameter will be used for whitelist/blacklist/percentage evaluation
     */
    String contextParam() default "";
    
    /**
     * Optional: custom message to show when feature is disabled
     */
    String disabledMessage() default "This feature is currently disabled";
    
    /**
     * Optional: redirect path when feature is disabled
     * If empty, will show error message
     */
    String disabledRedirect() default "";
}
