package org.springframework.samples.petclinic.TestUtils;

import lombok.NoArgsConstructor;
import org.springframework.samples.petclinic.Pet.Pet;
import org.springframework.samples.petclinic.owner.Owner;

import static org.springframework.samples.petclinic.TestUtils.PetTestUtil.createPet;

@NoArgsConstructor
public class OwnerTestUtil {

	public static Owner createOwner() {
		return createOwner(createPet());
	}

	public static Owner createOwner(Pet pet) {
		var owner = new Owner();
		owner.setId(1);
		owner.setFirstName("firstName");
		owner.setLastName("lastname");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
		owner.addPet(pet);
		return owner;
	}

	// TODO create custom Builder with Lombok (should also include Id from BaseEntity)
	public static Owner createOwner(Integer ownerId, String firstName, String lastname, Pet pet) {
		var owner = new Owner();
		owner.setId(ownerId);
		owner.setFirstName(firstName);
		owner.setLastName(lastname);
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
		owner.addPet(pet);
		return owner;
	}

}
