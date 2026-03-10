package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Owner.getPet() and related model methods
 */
public class OwnerPetVisitTest {

	@Test
	public void testGetPetByStringName() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setName("Fido");
		owner.addPet(pet);

		Pet retrieved = owner.getPet("Fido");
		assertNotNull(retrieved);
		assertEquals("Fido", retrieved.getName());
	}

	@Test
	public void testGetPetByStringNameNotFound() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setName("Fido");
		owner.addPet(pet);

		Pet retrieved = owner.getPet("Fluffy");
		assertNull(retrieved);
	}

	@Test
	public void testGetPetByStringNameCaseInsensitive() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setName("Fido");
		owner.addPet(pet);

		Pet retrieved = owner.getPet("FIDO");
		assertNotNull(retrieved);
		assertEquals("Fido", retrieved.getName());
	}

	@Test
	public void testGetPetWithIgnoreNewFalse() {
		Owner owner = new Owner();
		Pet newPet = new Pet();
		newPet.setName("NewPet");
		owner.addPet(newPet);

		Pet retrieved = owner.getPet("NewPet", false);
		assertNotNull(retrieved);
		assertEquals("NewPet", retrieved.getName());
	}

	@Test
	public void testGetPetWithIgnoreNewTrue() {
		Owner owner = new Owner();
		Pet newPet = new Pet();
		newPet.setName("NewPet");
		owner.addPet(newPet);

		Pet retrieved = owner.getPet("NewPet", true);
		assertNull(retrieved);
	}

	@Test
	public void testMultiplePetsGetCorrectOne() {
		Owner owner = new Owner();
		Pet pet1 = new Pet();
		pet1.setName("Fido");
		Pet pet2 = new Pet();
		pet2.setName("Fluffy");
		owner.addPet(pet1);
		owner.addPet(pet2);

		Pet retrieved = owner.getPet("Fluffy");
		assertNotNull(retrieved);
		assertEquals("Fluffy", retrieved.getName());
	}

}
