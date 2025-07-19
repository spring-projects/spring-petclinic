package org.springframework.samples.petclinic.vaccine.predicate;

import org.springframework.samples.petclinic.owner.Pet;

import java.util.function.Predicate;

public class PetPredicates {

	public static Predicate<Pet> isActive() {
		return pet -> Boolean.TRUE.equals(pet.getActive());
	}

	public static Predicate<Pet> hasId(Integer petId) {
		return pet -> petId.equals(pet.getId());
	}

}
