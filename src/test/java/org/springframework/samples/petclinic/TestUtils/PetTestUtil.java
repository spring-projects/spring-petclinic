package org.springframework.samples.petclinic.TestUtils;

import lombok.NoArgsConstructor;
import org.springframework.samples.petclinic.Pet.Pet;
import org.springframework.samples.petclinic.Pet.PetType;
import org.springframework.samples.petclinic.Pet.PetTypes;
import org.springframework.samples.petclinic.owner.Visit;

import java.time.LocalDate;

import static org.springframework.samples.petclinic.TestUtils.VisitTestUtil.createVisit;

@NoArgsConstructor
public class PetTestUtil {

	public static Pet createPet() {
		var petType = PetTypeTestUtil.createPetType(PetTypes.CAT.getValue());
		return createPet("Max", petType, createVisit());
	}

	public static Pet createPet(int petId) {
		var petType = PetTypeTestUtil.createPetType(PetTypes.CAT.getValue());
		return createPet(petId, "Max", petType, createVisit());
	}

	public static Pet createPet(String name, PetType petType) {
		return createPet(name, petType, createVisit());
	}

	public static Pet createPet(String name, PetType petType, Visit visit) {
		return createPet(1, name, petType, visit);
	}

	public static Pet createPet(int petId, String name, PetType petType, Visit visit) {
		var pet = new Pet();
		pet.setId(petId);
		pet.setType(petType);
		pet.setName(name);
		pet.setBirthDate(LocalDate.now());
		pet.addVisit(visit);
		return pet;
	}

}
