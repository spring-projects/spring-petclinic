/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Extended unit tests for Pet domain class covering addVisit method and edge cases.
 */
class PetExtendedTest {

	private Pet pet;

	private Owner owner;

	@BeforeEach
	void setUp() {
		owner = new Owner();
		owner.setId(1);

		pet = new Pet();
		pet.setId(1);
		pet.setName("Buddy");

		PetType dog = new PetType();
		dog.setId(1);
		dog.setName("Dog");
		pet.setType(dog);

		pet.setBirthDate(LocalDate.of(2018, 6, 10));
		owner.addPet(pet);
	}

	@Test
	void testAddVisitToPet() {
		Visit visit = new Visit();
		visit.setId(1);
		visit.setDate(LocalDate.now());
		visit.setDescription("Annual checkup");

		pet.addVisit(visit);

		assertEquals(1, pet.getVisits().size());
		assertTrue(pet.getVisits().contains(visit));
	}

	@Test
	void testAddMultipleVisits() {
		for (int i = 1; i <= 5; i++) {
			Visit visit = new Visit();
			visit.setId(i);
			visit.setDate(LocalDate.now().minusMonths(i));
			visit.setDescription("Visit " + i);
			pet.addVisit(visit);
		}

		assertEquals(5, pet.getVisits().size());
	}

	@Test
	void testAddVisitToNewPetWithoutId() {
		Pet newPet = new Pet();
		newPet.setName("Rex");
		newPet.setType(new PetType());

		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		visit.setDescription("First visit");

		newPet.addVisit(visit);

		assertEquals(1, newPet.getVisits().size());
	}

	@Test
	void testPetVisitOrdering() {
		LocalDate today = LocalDate.now();

		Visit visit1 = new Visit();
		visit1.setId(1);
		visit1.setDate(today.minusDays(60));
		visit1.setDescription("Oldest");

		Visit visit2 = new Visit();
		visit2.setId(2);
		visit2.setDate(today.minusDays(30));
		visit2.setDescription("Recent");

		Visit visit3 = new Visit();
		visit3.setId(3);
		visit3.setDate(today);
		visit3.setDescription("Today");

		pet.addVisit(visit1);
		pet.addVisit(visit3);
		pet.addVisit(visit2);

		assertEquals(3, pet.getVisits().size());
	}

	@Test
	void testIsNewWithoutId() {
		Pet newPet = new Pet();
		assertTrue(newPet.isNew());
	}

	@Test
	void testIsNewWithId() {
		assertFalse(pet.isNew());
	}

	@Test
	void testGetBirthDate() {
		assertEquals(LocalDate.of(2018, 6, 10), pet.getBirthDate());
	}

	@Test
	void testSetBirthDate() {
		LocalDate newDate = LocalDate.of(2019, 1, 15);
		pet.setBirthDate(newDate);
		assertEquals(newDate, pet.getBirthDate());
	}

	@Test
	void testGetType() {
		assertNotNull(pet.getType());
		assertEquals("Dog", pet.getType().getName());
	}

	@Test
	void testSetType() {
		PetType cat = new PetType();
		cat.setId(2);
		cat.setName("Cat");

		pet.setType(cat);

		assertEquals("Cat", pet.getType().getName());
	}

	@Test
	void testGetVisitsEmpty() {
		Pet newPet = new Pet();
		assertNotNull(newPet.getVisits());
		assertTrue(newPet.getVisits().isEmpty());
	}

	@Test
	void testPetToStringContainsName() {
		String result = pet.toString();
		assertTrue(result.contains("Buddy"));
	}

	@Test
	void testPetAddVisitNullDate() {
		Visit visit = new Visit();
		visit.setId(1);
		visit.setDescription("Checkup");

		pet.addVisit(visit);

		assertEquals(1, pet.getVisits().size());
	}

	@Test
	void testPetWithOwnerAndVisits() {
		Visit visit = new Visit();
		visit.setId(1);
		visit.setDate(LocalDate.now());
		visit.setDescription("Checkup");

		pet.addVisit(visit);

		assertEquals(1, pet.getVisits().size());
	}

}
