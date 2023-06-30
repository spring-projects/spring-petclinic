package org.springframework.samples.petclinic.owner;

public class PetNotFoundException extends RuntimeException {
	public PetNotFoundException(String message) {
		super(message);
	}
}
