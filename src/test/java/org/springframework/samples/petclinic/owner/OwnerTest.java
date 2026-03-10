package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OwnerTest {

	@Test
	public void testOwnerFirstAndLastName() {
		Owner owner = new Owner();
		owner.setFirstName("Joe");
		owner.setLastName("Smith");
		assertEquals("Joe", owner.getFirstName());
		assertEquals("Smith", owner.getLastName());
	}

	@Test
	public void testOwnerAddress() {
		Owner owner = new Owner();
		owner.setAddress("123 Main St");
		assertEquals("123 Main St", owner.getAddress());
	}

	@Test
	public void testOwnerCity() {
		Owner owner = new Owner();
		owner.setCity("Chicago");
		assertEquals("Chicago", owner.getCity());
	}

	@Test
	public void testOwnerTelephone() {
		Owner owner = new Owner();
		owner.setTelephone("1234567890");
		assertEquals("1234567890", owner.getTelephone());
	}

	@Test
	public void testOwnerAddPet() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setName("Fido");
		owner.addPet(pet);
		assertEquals(1, owner.getPets().size());
		assertTrue(owner.getPets().contains(pet));
	}

	@Test
	public void testOwnerMultiplePets() {
		Owner owner = new Owner();
		Pet pet1 = new Pet();
		Pet pet2 = new Pet();
		pet1.setName("Fido");
		pet2.setName("Fluffy");
		owner.addPet(pet1);
		owner.addPet(pet2);
		assertEquals(2, owner.getPets().size());
	}

	@Test
	public void testOwnerGetPetByName() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setName("Fido");
		owner.addPet(pet);
		Pet retrieved = owner.getPet("Fido");
		assertNotNull(retrieved);
		assertEquals("Fido", retrieved.getName());
	}

	@Test
	public void testOwnerGetPetByNameNotFound() {
		Owner owner = new Owner();
		Pet retrieved = owner.getPet("NonExistent");
		assertNull(retrieved);
	}

	@Test
	public void testOwnerCompleteFl() {
		Owner owner = new Owner();
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setAddress("100 Oak St");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
		assertEquals("John", owner.getFirstName());
		assertEquals("Doe", owner.getLastName());
		assertEquals("100 Oak St", owner.getAddress());
		assertEquals("Madison", owner.getCity());
		assertEquals("6085551023", owner.getTelephone());
	}

}
