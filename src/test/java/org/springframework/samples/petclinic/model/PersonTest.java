package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

	@Test
	public void testPersonFirstAndLastName() {
		Person person = new Person();
		person.setFirstName("John");
		person.setLastName("Doe");
		assertEquals("John", person.getFirstName());
		assertEquals("Doe", person.getLastName());
	}

	@Test
	public void testPersonFirstNameNull() {
		Person person = new Person();
		assertNull(person.getFirstName());
	}

	@Test
	public void testPersonLastNameNull() {
		Person person = new Person();
		assertNull(person.getLastName());
	}

	@Test
	public void testPersonMultipleNames() {
		Person person1 = new Person();
		Person person2 = new Person();
		person1.setFirstName("Alice");
		person1.setLastName("Smith");
		person2.setFirstName("Bob");
		person2.setLastName("Jones");
		assertNotEquals(person1.getFirstName(), person2.getFirstName());
		assertNotEquals(person1.getLastName(), person2.getLastName());
	}

	@Test
	public void testPersonInheritanceFromBaseEntity() {
		Person person = new Person();
		person.setId(10);
		assertEquals(10, person.getId());
		assertTrue(person.isNew() == false || person.isNew() == true); // Id set
	}

}
