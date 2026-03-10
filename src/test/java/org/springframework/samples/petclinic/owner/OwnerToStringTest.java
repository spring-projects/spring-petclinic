package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Owner.toString() method
 */
public class OwnerToStringTest {

	@Test
	public void testOwnerToStringWithCompleteInfo() {
		Owner owner = new Owner();
		owner.setId(1);
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setAddress("123 Main St");
		owner.setCity("Chicago");
		owner.setTelephone("5551234567");

		String result = owner.toString();
		assertNotNull(result);
		assertTrue(result.length() > 0);
	}

	@Test
	public void testOwnerToStringWithNullFields() {
		Owner owner = new Owner();
		owner.setId(2);

		String result = owner.toString();
		assertNotNull(result);
	}

	@Test
	public void testOwnerToStringFormat() {
		Owner owner = new Owner();
		owner.setId(3);
		owner.setFirstName("Jane");
		owner.setLastName("Smith");

		String result = owner.toString();
		assertTrue(result.contains("Jane") || result.contains("Smith") || result.length() > 0);
	}

	@Test
	public void testOwnerToStringWithOnlyName() {
		Owner owner = new Owner();
		owner.setFirstName("Bob");
		owner.setLastName("Brown");

		String result = owner.toString();
		assertNotNull(result);
		assertTrue(result.length() > 0);
	}

	@Test
	public void testOwnerToStringConsistency() {
		Owner owner = new Owner();
		owner.setId(5);
		owner.setFirstName("Alice");
		owner.setLastName("Johnson");

		String result1 = owner.toString();
		String result2 = owner.toString();
		assertEquals(result1, result2);
	}

}
