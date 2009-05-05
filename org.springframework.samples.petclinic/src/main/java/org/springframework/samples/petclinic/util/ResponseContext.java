package org.springframework.samples.petclinic.util;

public interface ResponseContext {

	void redirect(Object resource);

	ResponseContext forResource(Object resource);

}
