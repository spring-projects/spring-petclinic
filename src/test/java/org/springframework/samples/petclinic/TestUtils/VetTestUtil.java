package org.springframework.samples.petclinic.TestUtils;

import lombok.NoArgsConstructor;
import org.springframework.samples.petclinic.Pet.Pet;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;

import static org.springframework.samples.petclinic.TestUtils.PetTestUtil.createPet;

@NoArgsConstructor
public class VetTestUtil {

	public static Vet createVet() {
		var vet = new Vet();
		vet.setFirstName("James");
		vet.setLastName("Carter");
		vet.setId(1);
		return vet;
	}

	public static Vet createVetWithSpecialty() {
		var vet = createVet();
		vet.setId(vet.getId() + 1);
		vet.addSpecialty(createSpecialty());
		return vet;
	}

	public static Specialty createSpecialty() {
		return createSpecialty("radiology");
	}

	public static Specialty createSpecialty(String name) {
		var specialty = new Specialty();
		specialty.setId(1);
		specialty.setName(name);
		return specialty;
	}

}
