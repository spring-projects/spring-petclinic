package org.springframework.samples.petclinic.feature;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FeatureToggle {

	String value();

}
