package org.springframework.samples.petclinic.owner.expection;

public class PetNotFoundException extends RuntimeException {

	public PetNotFoundException(String message) {
		super(message);
	}

}
