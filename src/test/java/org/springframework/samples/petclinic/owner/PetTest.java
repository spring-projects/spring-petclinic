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

class PetTest {

	private Pet pet;

	private PetType petType;

	@BeforeEach
	void setUp() {
		pet = new Pet();
		petType = new PetType();
		petType.setName("Dog");
	}

	@Test
	void testPetCreation() {
		pet.setName("Buddy");
		pet.setBirthDate(LocalDate.of(2020, 1, 15));
		pet.setType(petType);

		assertEquals("Buddy", pet.getName());
		assertEquals(LocalDate.of(2020, 1, 15), pet.getBirthDate());
		assertEquals(petType, pet.getType());
	}

	@Test
	void testPetNameSetterGetter() {
		pet.setName("Max");
		assertEquals("Max", pet.getName());
	}

	@Test
	void testPetBirthDateSetterGetter() {
		LocalDate birthDate = LocalDate.of(2019, 5, 10);
		pet.setBirthDate(birthDate);
		assertEquals(birthDate, pet.getBirthDate());
	}

	@Test
	void testPetTypeSetterGetter() {
		pet.setType(petType);
		assertEquals(petType, pet.getType());
	}

	@Test
	void testPetVisits() {
		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		visit.setDescription("Checkup");
		pet.getVisits().add(visit);

		assertEquals(1, pet.getVisits().size());
		assertTrue(pet.getVisits().contains(visit));
	}

	@Test
	void testPetGetVisitsNotNull() {
		assertNotNull(pet.getVisits());
	}

	@Test
	void testPetAddMultipleVisits() {
		Visit visit1 = new Visit();
		visit1.setDate(LocalDate.of(2022, 1, 15));
		Visit visit2 = new Visit();
		visit2.setDate(LocalDate.of(2022, 2, 20));

		pet.getVisits().add(visit1);
		pet.getVisits().add(visit2);

		assertEquals(2, pet.getVisits().size());
	}

	@Test
	void testPetIsNew() {
		assertTrue(pet.isNew());
		pet.setId(1);
		assertFalse(pet.isNew());
	}

	@Test
	void testPetWithDifferentTypes() {
		PetType catType = new PetType();
		catType.setName("Cat");
		pet.setType(catType);

		assertEquals("Cat", pet.getType().getName());

		PetType dogType = new PetType();
		dogType.setName("Dog");
		pet.setType(dogType);

		assertEquals("Dog", pet.getType().getName());
	}

	@Test
	void testPetWithAllFieldsSet() {
		pet.setName("Buddy");
		pet.setBirthDate(LocalDate.of(2020, 1, 15));
		pet.setType(petType);
		pet.setId(5);

		assertEquals("Buddy", pet.getName());
		assertEquals(LocalDate.of(2020, 1, 15), pet.getBirthDate());
		assertEquals(petType, pet.getType());
		assertEquals(5, pet.getId());
	}

	@Test
	void testPetEmptyVisits() {
		assertEquals(0, pet.getVisits().size());
	}

	@Test
	void testPetToString() {
		pet.setName("Buddy");
		assertNotNull(pet.toString());
	}

	@Test
	void testPetVisitOrder() {
		Visit visit1 = new Visit();
		visit1.setDate(LocalDate.of(2022, 1, 15));
		Visit visit2 = new Visit();
		visit2.setDate(LocalDate.of(2022, 2, 20));

		pet.getVisits().add(visit1);
		pet.getVisits().add(visit2);

		assertEquals(2, pet.getVisits().size());
	}

	@Test
	void testPetBirthDateNull() {
		pet.setBirthDate(null);
		assertNull(pet.getBirthDate());
	}

	@Test
	void testPetTypeNull() {
		pet.setType(null);
		assertNull(pet.getType());
	}

}
