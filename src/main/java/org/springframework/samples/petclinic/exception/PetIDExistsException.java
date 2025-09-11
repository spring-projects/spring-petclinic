package org.springframework.samples.petclinic.exception;

public class PetIDExistsException extends Exception {
	public PetIDExistsException(String message) {
		super("Pet Id: "+message);
	}
}
