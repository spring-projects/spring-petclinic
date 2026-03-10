package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PetAdvancedTest {

	@Test
	public void testPetMultipleVisits() {
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("Fluffy");

		for (int i = 0; i < 10; i++) {
			Visit visit = new Visit();
			visit.setDate(LocalDate.now().plusDays(i));
			visit.setDescription("Visit " + i);
			pet.addVisit(visit);
		}

		assertEquals(10, pet.getVisits().size());
	}

	@Test
	public void testPetVisitsOrdering() {
		Pet pet = new Pet();

		Visit visit1 = new Visit();
		visit1.setDate(LocalDate.of(2024, 3, 15));
		visit1.setDescription("First visit");

		Visit visit2 = new Visit();
		visit2.setDate(LocalDate.of(2024, 1, 10));
		visit2.setDescription("Early visit");

		pet.addVisit(visit1);
		pet.addVisit(visit2);

		assertEquals(2, pet.getVisits().size());
	}

	@Test
	public void testPetTypeAssignmentAdvanced() {
		Pet pet = new Pet();
		PetType dogType = new PetType();
		dogType.setId(1);
		dogType.setName("Dog");

		pet.setType(dogType);
		assertEquals(dogType, pet.getType());
		assertEquals("Dog", pet.getType().getName());
	}

	@Test
	public void testPetBirthDateVariations() {
		Pet pet1 = new Pet();
		Pet pet2 = new Pet();

		pet1.setBirthDate(LocalDate.of(2020, 1, 1));
		pet2.setBirthDate(LocalDate.of(2023, 12, 31));

		assertEquals(LocalDate.of(2020, 1, 1), pet1.getBirthDate());
		assertEquals(LocalDate.of(2023, 12, 31), pet2.getBirthDate());
		assertNotEquals(pet1.getBirthDate(), pet2.getBirthDate());
	}

	@Test
	public void testPetIdAssignment() {
		Pet pet = new Pet();
		pet.setId(42);
		assertEquals(42, pet.getId());
		assertFalse(pet.isNew());
	}

	@Test
	public void testPetNewStatus() {
		Pet pet = new Pet();
		assertTrue(pet.isNew());
		pet.setId(1);
		assertFalse(pet.isNew());
	}

}
