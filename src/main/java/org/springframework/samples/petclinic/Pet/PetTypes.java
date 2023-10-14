package org.springframework.samples.petclinic.Pet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum PetTypes {

	CAT("cat", "pet.cat"), DOG("dog", "pet.dog"), LIZARD("lizard", "pet.lizard"), SNAKE("snake", "pet.snake"),
	BIRD("bird", "pet.bird"), HAMSTER("hamster", "pet.hamster");

	private final String value;

	private final String frontendTranslationKey;

	// TODO: Refactor so we will use this instead of taking all Types from db
	public static List<String> getPetTypeNames() {
		List<String> allPetTypeNames = new ArrayList<>();
		Arrays.stream(values()).map(PetTypes::getValue).forEach(petTypesName -> allPetTypeNames.add(petTypesName));
		return allPetTypeNames;
	}

}
