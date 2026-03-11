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
package org.springframework.samples.petclinic.vet;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Extended unit tests for Vet domain class covering getNrOfSpecialties edge cases.
 */
class VetExtendedTest {

	private Vet vet;

	@BeforeEach
	void setUp() {
		vet = new Vet();
		vet.setId(1);
		vet.setFirstName("James");
		vet.setLastName("Carter");
	}

	@Test
	void testGetNrOfSpecialtiesWithNoSpecialties() {
		int count = vet.getNrOfSpecialties();
		assertEquals(0, count);
	}

	@Test
	void testGetNrOfSpecialtiesWithOneSpecialty() {
		Specialty surgery = new Specialty();
		surgery.setId(1);
		surgery.setName("Surgery");

		vet.addSpecialty(surgery);

		int count = vet.getNrOfSpecialties();
		assertEquals(1, count);
	}

	@Test
	void testGetNrOfSpecialtiesWithMultipleSpecialties() {
		Specialty surgery = new Specialty();
		surgery.setId(1);
		surgery.setName("Surgery");

		Specialty dentistry = new Specialty();
		dentistry.setId(2);
		dentistry.setName("Dentistry");

		Specialty cardiology = new Specialty();
		cardiology.setId(3);
		cardiology.setName("Cardiology");

		vet.addSpecialty(surgery);
		vet.addSpecialty(dentistry);
		vet.addSpecialty(cardiology);

		int count = vet.getNrOfSpecialties();
		assertEquals(3, count);
	}

	@Test
	void testGetSpecialtiesReturnsListView() {
		Specialty surgery = new Specialty();
		surgery.setId(1);
		surgery.setName("Surgery");

		vet.addSpecialty(surgery);

		var specialties = vet.getSpecialties();
		assertEquals(1, specialties.size());
		assertTrue(specialties.contains(surgery));
	}

	@Test
	void testGetSpecialtiesInternalAllowsModification() {
		Specialty surgery = new Specialty();
		surgery.setId(1);
		surgery.setName("Surgery");

		vet.getSpecialtiesInternal().add(surgery);

		assertEquals(1, vet.getNrOfSpecialties());
	}

	@Test
	void testAddSameSpecialtyTwice() {
		Specialty surgery = new Specialty();
		surgery.setId(1);
		surgery.setName("Surgery");

		vet.addSpecialty(surgery);
		vet.addSpecialty(surgery);

		assertEquals(1, vet.getNrOfSpecialties());
	}

	@Test
	void testGetSpecialties() {
		Specialty surgery = new Specialty();
		surgery.setId(1);
		surgery.setName("Surgery");

		Specialty dentistry = new Specialty();
		dentistry.setId(2);
		dentistry.setName("Dentistry");

		vet.addSpecialty(surgery);
		vet.addSpecialty(dentistry);

		var specialties = vet.getSpecialties();
		assertEquals(2, specialties.size());
		assertTrue(specialties.contains(surgery));
		assertTrue(specialties.contains(dentistry));
	}

	@Test
	void testVetWithoutSpecialties() {
		assertEquals(0, vet.getNrOfSpecialties());
		assertNotNull(vet.getSpecialties());
		assertTrue(vet.getSpecialties().isEmpty());
	}

	@Test
	void testGetSpecialtiesInternalForRemoval() {
		Specialty surgery = new Specialty();
		surgery.setId(1);
		surgery.setName("Surgery");

		Specialty dentistry = new Specialty();
		dentistry.setId(2);
		dentistry.setName("Dentistry");

		vet.addSpecialty(surgery);
		vet.addSpecialty(dentistry);

		assertEquals(2, vet.getNrOfSpecialties());

		vet.getSpecialtiesInternal().remove(surgery);

		assertEquals(1, vet.getNrOfSpecialties());
	}

}
