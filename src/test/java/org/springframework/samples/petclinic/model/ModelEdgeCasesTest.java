package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ModelEdgeCasesTest {

	@Test
	public void testBaseEntityNullId() {
		BaseEntity entity = new BaseEntity();
		assertNull(entity.getId());
		assertTrue(entity.isNew());
	}

	@Test
	public void testBaseEntityZeroId() {
		BaseEntity entity = new BaseEntity();
		entity.setId(0);
		assertFalse(entity.isNew());
		assertEquals(0, entity.getId());
	}

	@Test
	public void testBaseEntityNegativeId() {
		BaseEntity entity = new BaseEntity();
		entity.setId(-1);
		assertFalse(entity.isNew());
		assertEquals(-1, entity.getId());
	}

	@Test
	public void testNamedEntityEmpty() {
		NamedEntity entity = new NamedEntity();
		assertNull(entity.getName());
		entity.setName("");
		assertEquals("", entity.getName());
	}

	@Test
	public void testNamedEntitySpecialCharacters() {
		NamedEntity entity = new NamedEntity();
		entity.setName("Test@#$%^&*()");
		assertEquals("Test@#$%^&*()", entity.getName());
	}

	@Test
	public void testPersonBothNames() {
		Person person = new Person();
		person.setFirstName("John");
		person.setLastName("Doe");
		assertEquals("John", person.getFirstName());
		assertEquals("Doe", person.getLastName());
	}

	@Test
	public void testPersonNoFirstName() {
		Person person = new Person();
		person.setLastName("Doe");
		assertNull(person.getFirstName());
		assertEquals("Doe", person.getLastName());
	}

	@Test
	public void testPersonNoLastName() {
		Person person = new Person();
		person.setFirstName("John");
		assertNull(person.getLastName());
		assertEquals("John", person.getFirstName());
	}

}
