package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PetUnitTest {

	private Pet pet;

	@BeforeEach
	void setUp() {
		pet = new Pet();
		pet.setId(1);
		pet.setName("Micio");
	}

	@Test
	void getVisits_WhenNoVisitsAdded_ReturnsEmptyCollection() {
		assertTrue(pet.getVisits().isEmpty());
	}

	@Test
	void addVisit_WhenVisitAdded_StoresVisitInCollection() {
		Visit visit = new Visit();
		visit.setDate(LocalDate.of(2026, 3, 15));
		visit.setDescription("Vaccinazione");

		pet.addVisit(visit);

		assertEquals(1, pet.getVisits().size());
		assertTrue(pet.getVisits().contains(visit));
	}

	@Test
	void setAndGetProperties_BirthDateAndType_ReturnsExpectedValues() {
		PetType cat = new PetType();
		cat.setId(2);
		cat.setName("cat");
		LocalDate birthDate = LocalDate.of(2023, 8, 20);

		pet.setType(cat);
		pet.setBirthDate(birthDate);

		assertEquals(cat, pet.getType());
		assertEquals("cat", pet.getType().getName());
		assertEquals(birthDate, pet.getBirthDate());
	}

}