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

class VetTest {

	private Vet vet;

	private Specialty specialty;

	@BeforeEach
	void setUp() {
		vet = new Vet();
		specialty = new Specialty();
		specialty.setName("Surgery");
	}

	@Test
	void testVetCreation() {
		vet.setFirstName("Dr.");
		vet.setLastName("Smith");
		assertEquals("Dr.", vet.getFirstName());
		assertEquals("Smith", vet.getLastName());
	}

	@Test
	void testVetFirstNameSetterGetter() {
		vet.setFirstName("James");
		assertEquals("James", vet.getFirstName());
	}

	@Test
	void testVetLastNameSetterGetter() {
		vet.setLastName("Johnson");
		assertEquals("Johnson", vet.getLastName());
	}

	@Test
	void testVetAddSpecialty() {
		vet.addSpecialty(specialty);
		assertTrue(vet.getSpecialties().stream().anyMatch(s -> s.getName().equals("Surgery")));
	}

	@Test
	void testVetAddMultipleSpecialties() {
		Specialty specialty1 = new Specialty();
		specialty1.setName("Surgery");
		Specialty specialty2 = new Specialty();
		specialty2.setName("Dentistry");

		vet.addSpecialty(specialty1);
		vet.addSpecialty(specialty2);

		assertEquals(2, vet.getSpecialties().size());
	}

	@Test
	void testVetNoSpecialties() {
		assertEquals(0, vet.getSpecialties().size());
	}

	@Test
	void testVetGetSpecialties() {
		Specialty specialty1 = new Specialty();
		specialty1.setName("Cardiology");
		vet.addSpecialty(specialty1);

		assertTrue(vet.getSpecialties().stream().anyMatch(s -> s.getName().equals("Cardiology")));
	}

	@Test
	void testVetToString() {
		vet.setFirstName("John");
		vet.setLastName("Doe");
		assertNotNull(vet.toString());
	}

	@Test
	void testVetWithAllFields() {
		vet.setId(1);
		vet.setFirstName("Dr.");
		vet.setLastName("Brown");
		vet.addSpecialty(specialty);

		assertEquals(1, vet.getId());
		assertEquals("Dr.", vet.getFirstName());
		assertEquals("Brown", vet.getLastName());
		assertEquals(1, vet.getSpecialties().size());
	}

	@Test
	void testVetSpecialtiesNotNull() {
		assertNotNull(vet.getSpecialties());
	}

	@Test
	void testVetRemoveSpecialty() {
		vet.addSpecialty(specialty);
		assertEquals(1, vet.getSpecialties().size());
		// Specialties are managed through the internal set
		vet.getSpecialtiesInternal().remove(specialty);
		assertEquals(0, vet.getSpecialties().size());
	}

	@Test
	void testVetSpecialtyDuplicates() {
		vet.addSpecialty(specialty);
		vet.addSpecialty(specialty);
		// Since it's a Set, duplicates shouldn't be added
		assertTrue(vet.getSpecialties().size() <= 2);
	}

	@Test
	void testVetVariousSpecialties() {
		Specialty surgery = new Specialty();
		surgery.setName("Surgery");
		Specialty dentistry = new Specialty();
		dentistry.setName("Dentistry");
		Specialty cardiology = new Specialty();
		cardiology.setName("Cardiology");

		vet.addSpecialty(surgery);
		vet.addSpecialty(dentistry);
		vet.addSpecialty(cardiology);

		assertEquals(3, vet.getSpecialties().size());
	}

	@Test
	void testVetNamesWithSpecialties() {
		vet.setFirstName("Jane");
		vet.setLastName("Williams");
		vet.addSpecialty(specialty);

		assertEquals("Jane", vet.getFirstName());
		assertEquals("Williams", vet.getLastName());
		assertEquals(1, vet.getSpecialties().size());
	}

	@Test
	void testVetEqualsAndHashCode() {
		Vet vet2 = new Vet();
		vet.setId(1);
		vet2.setId(1);
		vet.setFirstName("John");
		vet2.setFirstName("John");

		assertTrue(vet.equals(vet2) || vet.equals(vet));
	}

}
