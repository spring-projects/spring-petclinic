package org.springframework.samples.petclinic.config;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;


/**
 * Indicates that a component is eligible for registration when none of the {@linkplain
 * #value specified profiles} is active.
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(NotProfileCondition.class)
public @interface NotProfile {
	
	/**
	 * The set of profiles for which the annotated component should not be registered.
	 */
	String[] value();


}
