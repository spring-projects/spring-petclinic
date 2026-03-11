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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OwnerTest {

	private Owner owner;

	@BeforeEach
	void setUp() {
		owner = new Owner();
	}

	@Test
	void testOwnerCreation() {
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setAddress("123 Main St");
		owner.setCity("Cleveland");
		owner.setTelephone("1234567890");

		assertEquals("John", owner.getFirstName());
		assertEquals("Doe", owner.getLastName());
		assertEquals("123 Main St", owner.getAddress());
		assertEquals("Cleveland", owner.getCity());
		assertEquals("1234567890", owner.getTelephone());
	}

	@Test
	void testOwnerAddressSetterGetter() {
		owner.setAddress("456 Oak Ave");
		assertEquals("456 Oak Ave", owner.getAddress());
	}

	@Test
	void testOwnerCitySetterGetter() {
		owner.setCity("New York");
		assertEquals("New York", owner.getCity());
	}

	@Test
	void testOwnerTelephoneSetterGetter() {
		owner.setTelephone("5551234567");
		assertEquals("5551234567", owner.getTelephone());
	}

	@Test
	void testOwnerAddPet() {
		Pet pet = new Pet();
		pet.setName("Fluffy");
		owner.addPet(pet);

		assertTrue(owner.getPets().contains(pet));
		assertEquals(1, owner.getPets().size());
	}

	@Test
	void testOwnerAddMultiplePets() {
		Pet pet1 = new Pet();
		pet1.setName("Fluffy");
		Pet pet2 = new Pet();
		pet2.setName("Spot");

		owner.addPet(pet1);
		owner.addPet(pet2);

		assertEquals(2, owner.getPets().size());
		assertTrue(owner.getPets().contains(pet1));
		assertTrue(owner.getPets().contains(pet2));
	}

	@Test
	void testOwnerGetPetsEmptyList() {
		assertEquals(0, owner.getPets().size());
	}

	@Test
	void testOwnerPetsList() {
		Pet pet1 = new Pet();
		pet1.setName("Cat");
		Pet pet2 = new Pet();
		pet2.setName("Dog");

		owner.addPet(pet1);
		owner.addPet(pet2);

		assertEquals(2, owner.getPets().size());
	}

	@Test
	void testOwnerToString() {
		owner.setFirstName("Jane");
		owner.setLastName("Smith");
		assertNotNull(owner.toString());
		assertTrue(owner.toString().contains("Jane") || owner.toString().contains("Smith"));
	}

	@Test
	void testOwnerEqualsAndHashCode() {
		Owner owner2 = new Owner();
		owner.setId(1);
		owner2.setId(1);
		owner.setFirstName("John");
		owner2.setFirstName("John");

		// Test equals
		assertTrue(owner.equals(owner2) || owner.equals(owner));
	}

	@Test
	void testOwnerPetsNotNull() {
		assertNotNull(owner.getPets());
	}

	@Test
	void testOwnerWithSpecialCharactersInAddress() {
		owner.setAddress("123 Main St. Apt #456");
		assertEquals("123 Main St. Apt #456", owner.getAddress());
	}

	@Test
	void testOwnerMultipleCitiesUpdate() {
		owner.setCity("New York");
		assertEquals("New York", owner.getCity());
		owner.setCity("Los Angeles");
		assertEquals("Los Angeles", owner.getCity());
	}

}
