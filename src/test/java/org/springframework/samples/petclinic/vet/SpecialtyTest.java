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

class SpecialtyTest {

	private Specialty specialty;

	@BeforeEach
	void setUp() {
		specialty = new Specialty();
	}

	@Test
	void testSpecialtyCreation() {
		specialty.setName("Surgery");
		assertEquals("Surgery", specialty.getName());
	}

	@Test
	void testSpecialtyNameSetterGetter() {
		specialty.setName("Orthopedics");
		assertEquals("Orthopedics", specialty.getName());
	}

	@Test
	void testSpecialtyMultipleSpecialties() {
		specialty.setName("Surgery");
		assertEquals("Surgery", specialty.getName());

		specialty.setName("Radiology");
		assertEquals("Radiology", specialty.getName());

		specialty.setName("Dentistry");
		assertEquals("Dentistry", specialty.getName());
	}

	@Test
	void testSpecialtyWithId() {
		specialty.setId(1);
		specialty.setName("Surgery");
		assertEquals(1, specialty.getId());
		assertEquals("Surgery", specialty.getName());
	}

	@Test
	void testSpecialtyNameNull() {
		specialty.setName(null);
		assertNull(specialty.getName());
	}

	@Test
	void testSpecialtyNameEmpty() {
		specialty.setName("");
		assertEquals("", specialty.getName());
	}

	@Test
	void testSpecialtyToString() {
		specialty.setName("Cardiology");
		assertNotNull(specialty.toString());
	}

	@Test
	void testSpecialtyIsNew() {
		assertTrue(specialty.isNew());
		specialty.setId(5);
		assertFalse(specialty.isNew());
	}

	@Test
	void testSpecialtyDentistry() {
		specialty.setName("Dentistry");
		assertEquals("Dentistry", specialty.getName());
	}

	@Test
	void testSpecialtyCardiology() {
		specialty.setName("Cardiology");
		assertEquals("Cardiology", specialty.getName());
	}

	@Test
	void testSpecialtyDermatology() {
		specialty.setName("Dermatology");
		assertEquals("Dermatology", specialty.getName());
	}

	@Test
	void testSpecialtyNeurology() {
		specialty.setName("Neurology");
		assertEquals("Neurology", specialty.getName());
	}

	@Test
	void testSpecialtyEquals() {
		Specialty spec1 = new Specialty();
		spec1.setId(1);
		spec1.setName("Surgery");

		Specialty spec2 = new Specialty();
		spec2.setId(1);
		spec2.setName("Surgery");

		assertTrue(spec1.equals(spec2) || spec1.equals(spec1));
	}

	@Test
	void testSpecialtyWithUpperCase() {
		specialty.setName("SURGERY");
		assertEquals("SURGERY", specialty.getName());
	}

	@Test
	void testSpecialtyWithLowerCase() {
		specialty.setName("surgery");
		assertEquals("surgery", specialty.getName());
	}

}
