package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NamedEntityTest {

	@Test
	public void testNamedEntityName() {
		NamedEntity entity = new NamedEntity();
		entity.setName("Test Name");
		assertEquals("Test Name", entity.getName());
	}

	@Test
	public void testNamedEntityNameNull() {
		NamedEntity entity = new NamedEntity();
		assertNull(entity.getName());
	}

	@Test
	public void testNamedEntityDifferentNames() {
		NamedEntity entity1 = new NamedEntity();
		NamedEntity entity2 = new NamedEntity();
		entity1.setName("Name One");
		entity2.setName("Name Two");
		assertNotEquals(entity1.getName(), entity2.getName());
	}

	@Test
	public void testNamedEntityEmpty() {
		NamedEntity entity = new NamedEntity();
		entity.setName("");
		assertEquals("", entity.getName());
	}

}
