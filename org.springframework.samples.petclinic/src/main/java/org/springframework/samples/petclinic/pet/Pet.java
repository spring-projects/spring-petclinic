package org.springframework.samples.petclinic.pet;

import java.util.Date;

import org.springframework.samples.petclinic.util.Measurement;

public class Pet {

	private String name;

	private String species;
	
	private String breed;

	private Gender gender;
	
	private Date birthDate;

	private Measurement weight;
	
	public String getName() {
		return name;
	}

}
