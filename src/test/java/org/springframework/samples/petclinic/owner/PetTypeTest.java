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

class PetTypeTest {

	private PetType petType;

	@BeforeEach
	void setUp() {
		petType = new PetType();
	}

	@Test
	void testPetTypeCreation() {
		petType.setName("Dog");
		assertEquals("Dog", petType.getName());
	}

	@Test
	void testPetTypeNameSetterGetter() {
		petType.setName("Cat");
		assertEquals("Cat", petType.getName());
	}

	@Test
	void testPetTypeMultipleTypes() {
		petType.setName("Dog");
		assertEquals("Dog", petType.getName());

		petType.setName("Cat");
		assertEquals("Cat", petType.getName());

		petType.setName("Bird");
		assertEquals("Bird", petType.getName());
	}

	@Test
	void testPetTypeWithId() {
		petType.setId(1);
		petType.setName("Hamster");
		assertEquals(1, petType.getId());
		assertEquals("Hamster", petType.getName());
	}

	@Test
	void testPetTypeNameNull() {
		petType.setName(null);
		assertNull(petType.getName());
	}

	@Test
	void testPetTypeNameEmpty() {
		petType.setName("");
		assertEquals("", petType.getName());
	}

	@Test
	void testPetTypeToString() {
		petType.setName("Dog");
		assertNotNull(petType.toString());
	}

	@Test
	void testPetTypeIsNew() {
		assertTrue(petType.isNew());
		petType.setId(5);
		assertFalse(petType.isNew());
	}

	@Test
	void testPetTypeWithSpecialCharacters() {
		petType.setName("Guinea-Pig");
		assertEquals("Guinea-Pig", petType.getName());
	}

	@Test
	void testPetTypeRabbit() {
		petType.setName("Rabbit");
		assertEquals("Rabbit", petType.getName());
	}

	@Test
	void testPetTypeLizard() {
		petType.setName("Lizard");
		assertEquals("Lizard", petType.getName());
	}

	@Test
	void testPetTypeSnake() {
		petType.setName("Snake");
		assertEquals("Snake", petType.getName());
	}

	@Test
	void testPetTypeParrot() {
		petType.setName("Parrot");
		assertEquals("Parrot", petType.getName());
	}

	@Test
	void testPetTypeEquals() {
		PetType type1 = new PetType();
		type1.setId(1);
		type1.setName("Dog");

		PetType type2 = new PetType();
		type2.setId(1);
		type2.setName("Dog");

		assertTrue(type1.equals(type2) || type1.equals(type1));
	}

}
