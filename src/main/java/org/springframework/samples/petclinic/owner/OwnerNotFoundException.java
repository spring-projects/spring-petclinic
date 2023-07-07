package org.springframework.samples.petclinic.owner;

public class OwnerNotFoundException extends RuntimeException {
	public OwnerNotFoundException(String message) {
		super(message);
	}
}
