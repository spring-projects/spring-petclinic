package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PetTypeFormatterTest {

	@Test
	public void testPetTypeCreation() {
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Dog");
		assertEquals(1, petType.getId());
		assertEquals("Dog", petType.getName());
	}

	@Test
	public void testPetTypeId() {
		PetType petType = new PetType();
		petType.setId(5);
		assertEquals(5, petType.getId());
	}

	@Test
	public void testPetTypeName() {
		PetType petType = new PetType();
		petType.setName("Hamster");
		assertEquals("Hamster", petType.getName());
	}

}
