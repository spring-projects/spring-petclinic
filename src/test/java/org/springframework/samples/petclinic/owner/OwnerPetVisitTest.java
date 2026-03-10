package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

/**
 * Tests for Owner.getPet() and addVisit() methods
 */
public class OwnerPetVisitTest {

	@Test
	public void testGetPetByIntegerId() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("Fido");
		owner.addPet(pet);

		Pet retrieved = owner.getPet(1);
		assertNotNull(retrieved);
		assertEquals("Fido", retrieved.getName());
	}

	@Test
	public void testGetPetByIntegerIdNotFound() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(1);
		owner.addPet(pet);

		Pet retrieved = owner.getPet(999);
		assertNull(retrieved);
	}

	@Test
	public void testGetPetByIntegerIdMultiplePets() {
		Owner owner = new Owner();
		Pet pet1 = new Pet();
		pet1.setId(1);
		pet1.setName("Fido");
		Pet pet2 = new Pet();
		pet2.setId(2);
		pet2.setName("Fluffy");
		owner.addPet(pet1);
		owner.addPet(pet2);

		Pet retrieved = owner.getPet(2);
		assertNotNull(retrieved);
		assertEquals("Fluffy", retrieved.getName());
	}

	@Test
	public void testAddVisit() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("Fido");
		owner.addPet(pet);

		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		visit.setDescription("Checkup");

		owner.addVisit(1, visit);

		assertEquals(1, pet.getVisits().size());
		assertTrue(pet.getVisits().contains(visit));
	}

	@Test
	public void testAddVisitMultipleVisits() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(1);
		owner.addPet(pet);

		Visit visit1 = new Visit();
		visit1.setDescription("First visit");
		Visit visit2 = new Visit();
		visit2.setDescription("Second visit");

		owner.addVisit(1, visit1);
		owner.addVisit(1, visit2);

		assertEquals(2, pet.getVisits().size());
	}

	@Test
	public void testAddVisitWithCustomDate() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(1);
		owner.addPet(pet);

		Visit visit = new Visit();
		LocalDate customDate = LocalDate.of(2023, 12, 25);
		visit.setDate(customDate);
		visit.setDescription("Holiday checkup");

		owner.addVisit(1, visit);

		assertEquals(1, pet.getVisits().size());
		Visit retrieved = (Visit) pet.getVisits().iterator().next();
		assertEquals(customDate, retrieved.getDate());
	}

	@Test
	public void testAddVisitToNonexistentPetDoesNothing() {
		Owner owner = new Owner();
		Visit visit = new Visit();
		visit.setDescription("Visit to nonexistent pet");

		// Should not throw exception
		try {
			owner.addVisit(999, visit);
		}
		catch (Exception e) {
			fail("Should not throw exception");
		}
	}

}
