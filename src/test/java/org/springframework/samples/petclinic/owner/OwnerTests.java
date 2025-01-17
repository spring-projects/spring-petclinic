package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OwnerTests {

	@Test
	public void testSetAndGetAddress() {
		Owner owner = new Owner();
		String address = "123 Main St";

		owner.setAddress(address);

		assertThat(owner.getAddress()).isEqualTo(address);
	}

	@Test
	public void testSetAndGetCity() {
		Owner owner = new Owner();
		String city = "Springfield";

		owner.setCity(city);

		assertThat(owner.getCity()).isEqualTo(city);
	}

	@Test
	public void testSetAndGetTelephone() {
		Owner owner = new Owner();
		String telephone = "1234567890";

		owner.setTelephone(telephone);

		assertThat(owner.getTelephone()).isEqualTo(telephone);
	}

	@Test
	public void testAddPet() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setName("Buddy");

		owner.addPet(pet);

		List<Pet> pets = owner.getPets();
		assertThat(pets).contains(pet);
	}

	@Test
	public void testGetPetByName() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setName("Buddy");
		owner.addPet(pet);

		Pet foundPet = owner.getPet("Buddy");

		assertThat(foundPet).isEqualTo(pet);
	}

	@Test
	public void testGetPetById() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setName("Buddy"); // Do not set the ID yet

		owner.addPet(pet);

		// Now set the ID manually after adding to the list
		pet.setId(1);

		Pet foundPet = owner.getPet(1);

		assertThat(foundPet).isEqualTo(pet);
	}

	@Test
	public void testAddVisitToPet() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		owner.addPet(pet); // Add the pet without an ID

		pet.setId(1); // Set the ID manually after adding to the list

		Visit visit = new Visit();
		visit.setDescription("Check-up");

		owner.addVisit(1, visit);

		assertThat(pet.getVisits()).contains(visit);
	}

	@Test
	public void testAddVisitWithInvalidPetId() {
		Owner owner = new Owner();
		Visit visit = new Visit();
		visit.setDescription("Check-up");

		assertThatThrownBy(() -> owner.addVisit(999, visit))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Invalid Pet identifier!");
	}

	@Test
	public void testToString() {
		Owner owner = new Owner();
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setAddress("123 Main St");
		owner.setCity("Springfield");
		owner.setTelephone("1234567890");

		String result = owner.toString();

		assertThat(result)
			.contains("John", "Doe", "123 Main St", "Springfield", "1234567890");
	}
}

