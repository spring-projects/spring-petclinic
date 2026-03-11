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
package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonTest {

	private Person person;

	@BeforeEach
	void setUp() {
		person = new Person();
	}

	@Test
	void testPersonCreation() {
		person.setFirstName("John");
		person.setLastName("Doe");

		assertEquals("John", person.getFirstName());
		assertEquals("Doe", person.getLastName());
	}

	@Test
	void testPersonFirstNameSetterGetter() {
		person.setFirstName("Jane");
		assertEquals("Jane", person.getFirstName());
	}

	@Test
	void testPersonLastNameSetterGetter() {
		person.setLastName("Smith");
		assertEquals("Smith", person.getLastName());
	}

	@Test
	void testPersonWithId() {
		person.setId(1);
		person.setFirstName("Bob");
		person.setLastName("Johnson");

		assertEquals(1, person.getId());
		assertEquals("Bob", person.getFirstName());
		assertEquals("Johnson", person.getLastName());
	}

	@Test
	void testPersonFirstNameNull() {
		person.setFirstName(null);
		assertNull(person.getFirstName());
	}

	@Test
	void testPersonLastNameNull() {
		person.setLastName(null);
		assertNull(person.getLastName());
	}

	@Test
	void testPersonFirstNameEmpty() {
		person.setFirstName("");
		assertEquals("", person.getFirstName());
	}

	@Test
	void testPersonLastNameEmpty() {
		person.setLastName("");
		assertEquals("", person.getLastName());
	}

	@Test
	void testPersonIsNew() {
		assertTrue(person.isNew());
		person.setId(5);
		assertFalse(person.isNew());
	}

	@Test
	void testPersonWithSpecialCharactersInName() {
		person.setFirstName("Jean-Pierre");
		person.setLastName("O'Brien");
		assertEquals("Jean-Pierre", person.getFirstName());
		assertEquals("O'Brien", person.getLastName());
	}

	@Test
	void testPersonMultipleNameUpdates() {
		person.setFirstName("John");
		person.setLastName("Smith");
		assertEquals("John", person.getFirstName());
		assertEquals("Smith", person.getLastName());

		person.setFirstName("Jane");
		person.setLastName("Doe");
		assertEquals("Jane", person.getFirstName());
		assertEquals("Doe", person.getLastName());
	}

	@Test
	void testPersonWithUnicodeNames() {
		person.setFirstName("José");
		person.setLastName("García");
		assertEquals("José", person.getFirstName());
		assertEquals("García", person.getLastName());
	}

	@Test
	void testPersonWithLongNames() {
		String longFirstName = "Christopher";
		String longLastName = "Williamson";
		person.setFirstName(longFirstName);
		person.setLastName(longLastName);
		assertEquals(longFirstName, person.getFirstName());
		assertEquals(longLastName, person.getLastName());
	}

	@Test
	void testPersonEquality() {
		Person person1 = new Person();
		person1.setId(1);
		person1.setFirstName("John");
		person1.setLastName(null);

		Person person2 = new Person();
		person2.setId(1);
		person2.setFirstName("John");
		person2.setLastName(null);

		assertEquals(person1.getId(), person2.getId());
		assertEquals(person1.getFirstName(), person2.getFirstName());
	}

	@Test
	void testPersonExtendBaseEntity() {
		assertTrue(person instanceof BaseEntity);
	}

}
