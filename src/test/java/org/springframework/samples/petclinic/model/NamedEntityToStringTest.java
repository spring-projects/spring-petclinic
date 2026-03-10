package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for NamedEntity toString() method
 */
public class NamedEntityToStringTest {

	@Test
	public void testNamedEntityToStringWithId() {
		NamedEntity entity = new NamedEntity();
		entity.setId(1);
		entity.setName("Test Entity");
		String result = entity.toString();
		assertNotNull(result);
		assertTrue(result.contains("Test Entity"));
	}

	@Test
	public void testNamedEntityToStringWithoutId() {
		NamedEntity entity = new NamedEntity();
		entity.setName("Test Entity No ID");
		String result = entity.toString();
		assertNotNull(result);
		assertTrue(result.contains("Test Entity No ID"));
	}

	@Test
	public void testNamedEntityToStringWithNullName() {
		NamedEntity entity = new NamedEntity();
		entity.setId(5);
		String result = entity.toString();
		assertNotNull(result);
	}

	@Test
	public void testNamedEntityToStringMultipleCalls() {
		NamedEntity entity = new NamedEntity();
		entity.setId(10);
		entity.setName("Multiple Calls");
		String result1 = entity.toString();
		String result2 = entity.toString();
		assertEquals(result1, result2);
	}

}
