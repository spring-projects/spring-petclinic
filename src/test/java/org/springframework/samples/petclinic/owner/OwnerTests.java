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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Owner model class.
 */
@DisplayName("Owner Model Tests")
class OwnerTests {

	private Owner owner;

	private Pet pet;

	private PetType petType;

	private Visit visit;

	@BeforeEach
	void setUp() {
		owner = new Owner();
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setAddress("123 Main St");
		owner.setCity("Springfield");
		owner.setTelephone("5551234567");

		petType = new PetType();
		petType.setName("Dog");

		pet = new Pet();
		pet.setName("Buddy");
		pet.setType(petType);
		pet.setBirthDate(LocalDate.now().minusYears(2));

		visit = new Visit();
		visit.setDate(LocalDate.now());
		visit.setDescription("Routine checkup");
	}

	// ==================== Address Tests ====================
	@Test
	@DisplayName("Should set and get address")
	void testSetAndGetAddress() {
		assertEquals("123 Main St", owner.getAddress());
		owner.setAddress("456 Oak Ave");
		assertEquals("456 Oak Ave", owner.getAddress());
	}

	@Test
	@DisplayName("Should handle null address")
	void testNullAddress() {
		owner.setAddress(null);
		assertNull(owner.getAddress());
	}

	@Test
	@DisplayName("Should handle empty address")
	void testEmptyAddress() {
		owner.setAddress("");
		assertEquals("", owner.getAddress());
	}

	// ==================== City Tests ====================
	@Test
	@DisplayName("Should set and get city")
	void testSetAndGetCity() {
		assertEquals("Springfield", owner.getCity());
		owner.setCity("New York");
		assertEquals("New York", owner.getCity());
	}

	@Test
	@DisplayName("Should handle null city")
	void testNullCity() {
		owner.setCity(null);
		assertNull(owner.getCity());
	}

	@Test
	@DisplayName("Should handle empty city")
	void testEmptyCity() {
		owner.setCity("");
		assertEquals("", owner.getCity());
	}

	// ==================== Telephone Tests ====================
	@Test
	@DisplayName("Should set and get telephone")
	void testSetAndGetTelephone() {
		assertEquals("5551234567", owner.getTelephone());
		owner.setTelephone("5559876543");
		assertEquals("5559876543", owner.getTelephone());
	}

	@Test
	@DisplayName("Should handle null telephone")
	void testNullTelephone() {
		owner.setTelephone(null);
		assertNull(owner.getTelephone());
	}

	@Test
	@DisplayName("Should handle empty telephone")
	void testEmptyTelephone() {
		owner.setTelephone("");
		assertEquals("", owner.getTelephone());
	}

	// ==================== Pet Management Tests ====================
	@Test
	@DisplayName("Should initialize with empty pet list")
	void testInitialPetsEmpty() {
		Owner newOwner = new Owner();
		assertNotNull(newOwner.getPets());
		assertTrue(newOwner.getPets().isEmpty());
	}

	@Test
	@DisplayName("Should add new pet successfully")
	void testAddNewPet() {
		owner.addPet(pet);
		assertEquals(1, owner.getPets().size());
		assertEquals(pet, owner.getPets().get(0));
	}

	@Test
	@DisplayName("Should not add existing pet")
	void testDoNotAddExistingPet() {
		pet.setId(1);
		int initialSize = owner.getPets().size();
		owner.addPet(pet);
		assertEquals(initialSize, owner.getPets().size());
	}

	@Test
	@DisplayName("Should add multiple new pets")
	void testAddMultiplePets() {
		Pet pet1 = new Pet();
		pet1.setName("Buddy");
		Pet pet2 = new Pet();
		pet2.setName("Max");

		owner.addPet(pet1);
		owner.addPet(pet2);

		assertEquals(2, owner.getPets().size());
		assertEquals("Buddy", owner.getPets().get(0).getName());
		assertEquals("Max", owner.getPets().get(1).getName());
	}

	@Test
	@DisplayName("Should get pet by name")
	void testGetPetByName() {
		owner.addPet(pet);
		Pet found = owner.getPet("Buddy");
		assertNotNull(found);
		assertEquals("Buddy", found.getName());
	}

	@Test
	@DisplayName("Should get pet by name case-insensitive")
	void testGetPetByNameCaseInsensitive() {
		owner.addPet(pet);
		Pet found = owner.getPet("BUDDY");
		assertNotNull(found);
		assertEquals("Buddy", found.getName());

		found = owner.getPet("buddy");
		assertNotNull(found);
		assertEquals("Buddy", found.getName());
	}

	@Test
	@DisplayName("Should return null when pet name not found")
	void testGetPetByNameNotFound() {
		owner.addPet(pet);
		Pet found = owner.getPet("Fluffy");
		assertNull(found);
	}

	@Test
	@DisplayName("Should return null when searching in empty pet list")
	void testGetPetByNameEmptyList() {
		Pet found = owner.getPet("Buddy");
		assertNull(found);
	}

	@Test
	@DisplayName("Should get pet by id")
	void testGetPetById() {
		owner.addPet(pet);
		pet.setId(1);
		Pet found = owner.getPet(1);
		assertNotNull(found);
		assertEquals("Buddy", found.getName());
	}

	@Test
	@DisplayName("Should return null when pet id not found")
	void testGetPetByIdNotFound() {
		pet.setId(1);
		owner.addPet(pet);
		Pet found = owner.getPet(999);
		assertNull(found);
	}

	@Test
	@DisplayName("Should return null when searching by id in empty list")
	void testGetPetByIdEmptyList() {
		Pet found = owner.getPet(1);
		assertNull(found);
	}

	@Test
	@DisplayName("Should not find pet by id if still new")
	void testGetPetByIdIgnoresNewPets() {
		owner.addPet(pet);
		Pet found = owner.getPet(1);
		assertNull(found);
	}

	@Test
	@DisplayName("Should get pet by name ignoring new pets")
	void testGetPetByNameIgnoreNewPets() {
		owner.addPet(pet);
		Pet found = owner.getPet("Buddy", true);
		assertNull(found);
	}

	@Test
	@DisplayName("Should get pet by name including new pets")
	void testGetPetByNameIncludeNewPets() {
		owner.addPet(pet);
		Pet found = owner.getPet("Buddy", false);
		assertNotNull(found);
		assertEquals("Buddy", found.getName());
	}

	@Test
	@DisplayName("Should return null when name is null")
	void testGetPetByNullName() {
		owner.addPet(pet);
		Pet found = owner.getPet((String) null);
		assertNull(found);
	}

	@Test
	@DisplayName("Should return null when pet name is null")
	void testGetPetWithNullPetName() {
		pet.setName(null);
		owner.addPet(pet);
		Pet found = owner.getPet("Buddy");
		assertNull(found);
	}

	// ==================== Visit Management Tests ====================
	@Test
	@DisplayName("Should add visit to pet successfully")
	void testAddVisitToPet() {
		owner.addPet(pet);
		pet.setId(1);
		owner.addVisit(1, visit);
		assertEquals(1, pet.getVisits().size());
		assertEquals(visit, pet.getVisits().iterator().next());
	}

	@Test
	@DisplayName("Should throw exception when pet id is null")
	void testAddVisitWithNullPetId() {
		assertThrows(IllegalArgumentException.class, () -> {
			owner.addVisit(null, visit);
		});
	}

	@Test
	@DisplayName("Should throw exception when visit is null")
	void testAddVisitWithNullVisit() {
		owner.addPet(pet);
		pet.setId(1);
		assertThrows(IllegalArgumentException.class, () -> {
			owner.addVisit(1, null);
		});
	}

	@Test
	@DisplayName("Should throw exception when pet not found")
	void testAddVisitPetNotFound() {
		owner.addPet(pet);
		pet.setId(1);
		assertThrows(IllegalArgumentException.class, () -> {
			owner.addVisit(999, visit);
		});
	}

	@Test
	@DisplayName("Should add multiple visits to same pet")
	void testAddMultipleVisits() {
		owner.addPet(pet);
		pet.setId(1);

		Visit visit1 = new Visit();
		visit1.setDate(LocalDate.now());
		visit1.setDescription("Checkup 1");

		Visit visit2 = new Visit();
		visit2.setDate(LocalDate.now().plusDays(1));
		visit2.setDescription("Checkup 2");

		owner.addVisit(1, visit1);
		owner.addVisit(1, visit2);

		assertEquals(2, pet.getVisits().size());
	}

	// ==================== Inheritance and toString Tests ====================
	@Test
	@DisplayName("Should inherit from Person")
	void testOwnerInheritsPerson() {
		owner.setFirstName("John");
		owner.setLastName("Doe");
		assertEquals("John", owner.getFirstName());
		assertEquals("Doe", owner.getLastName());
	}

	@Test
	@DisplayName("Should generate toString representation")
	void testToString() {
		String result = owner.toString();
		assertNotNull(result);
		assertTrue(result.contains("Doe"));
		assertTrue(result.contains("John"));
		assertTrue(result.contains("Springfield"));
		assertTrue(result.contains("5551234567"));
	}

	@Test
	@DisplayName("Should include address and city in toString")
	void testToStringIncludesDetails() {
		owner.setAddress("123 Test Street");
		owner.setCity("Test City");
		String result = owner.toString();
		assertTrue(result.contains("Test Street"));
		assertTrue(result.contains("Test City"));
	}

	// ==================== Edge Case Tests ====================
	@Test
	@DisplayName("Should handle finding pet when multiple pets have similar names")
	void testGetPetWithSimilarNames() {
		Pet buddy1 = new Pet();
		buddy1.setName("Buddy");
		Pet buddy2 = new Pet();
		buddy2.setName("Buddy2");

		owner.addPet(buddy1);
		owner.addPet(buddy2);

		Pet found = owner.getPet("Buddy");
		assertNotNull(found);
		assertEquals("Buddy", found.getName());
	}

	@Test
	@DisplayName("Should handle empty string pet name")
	void testGetPetWithEmptyName() {
		pet.setName("");
		owner.addPet(pet);
		Pet found = owner.getPet("");
		assertNotNull(found);
	}

	@Test
	@DisplayName("Should handle whitespace in pet names")
	void testGetPetWithWhitespace() {
		pet.setName("  Buddy  ");
		owner.addPet(pet);
		Pet found = owner.getPet("  buddy  ");
		assertNotNull(found);
	}

	@Test
	@DisplayName("Pets list should not be null")
	void testPetsListNotNull() {
		Owner newOwner = new Owner();
		assertNotNull(newOwner.getPets());
	}

}
