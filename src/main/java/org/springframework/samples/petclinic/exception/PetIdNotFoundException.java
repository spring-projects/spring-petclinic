package org.springframework.samples.petclinic.exception;

public class PetIdNotFoundException extends Exception {

	public PetIdNotFoundException(String message){
		super("Pet Id: "+message);
	}
}
