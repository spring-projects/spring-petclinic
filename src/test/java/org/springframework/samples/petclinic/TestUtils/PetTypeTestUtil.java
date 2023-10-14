package org.springframework.samples.petclinic.TestUtils;

import org.springframework.samples.petclinic.Pet.PetType;
import org.springframework.samples.petclinic.Pet.PetTypes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PetTypeTestUtil {

	public static PetType createPetType(String petTypeName) {
		return createPetType(1, petTypeName);
	}

	public static PetType createPetType(int id, String petTypeName) {
		var petType = new PetType();
		petType.setName(petTypeName);
		petType.setId(id);
		return petType;
	}

	public static List<PetType> createPetTypes(PetTypes... petTypes) {
		return Stream.of(petTypes).map(PetTypes::getValue).map(petTypeName -> {
			var newpetType = new PetType();
			newpetType.setName(petTypeName);
			return newpetType;
		}).collect(Collectors.toList());
	}

}
